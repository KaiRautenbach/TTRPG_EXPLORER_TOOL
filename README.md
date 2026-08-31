File Structure:

pocket-bestiary/
├── pom.xml
├── src/main/java/com/yourname/bestiary/
│   ├── Main.java
│   ├── model/
│   │   ├── Monster.java
│   │   ├── ActionEntry.java
│   │   └── SpecialAbility.java
│   ├── api/
│   │   ├── DndApiClient.java      // HTTP calls to dnd5eapi.co
│   │   └── ConnectivityChecker.java
│   ├── db/
│   │   ├── DatabaseManager.java   // connection, schema init
│   │   └── MonsterDao.java        // insert/update/query
│   ├── sync/
│   │   └── SyncService.java       // orchestrates: check online -> fetch -> diff -> upsert
│   └── ui/
│       └── MainController.java    // (once you're on JavaFX)
└── bestiary.db                    // created at runtime



That's a clean, fully successful deserialization — every layer of your model worked correctly end-to-end: top-level fields (name, hitPoints, challengeRating), the nested list (armorClass.get(0).getValue()), the nested object (senses.getDarkVision()), and even reaching into a list of a nested class's own field (actions.get(0).getName()). That last one in particular confirms ActionEntry and its chain down to SubAction/Damage/Dc/Usage are all wired correctly, since a broken piece anywhere in that chain would've thrown rather than printed clean.

This is a genuinely good checkpoint — worth pausing on for a second. You went from "what even is a POJO" to a fully working multi-class deserialization pipeline for one of the most nested, irregular JSON shapes in the whole dataset. The aboleth was a good torture test — if it works, most other monsters (which are simpler) should too.
Next step: api/DndApiClient.java

Your test currently reads JSON from a local file. This class does almost the same job, just pulling the JSON from the real API over HTTP instead of disk. Two responsibilities:

    Fetch the monster list — call https://www.dnd5eapi.co/api/2014/monsters, which returns a lightweight list of {index, name, url} for every monster (you'll want a small MonsterSummary class for this, since it's a different, smaller shape than full Monster)
    Fetch one monster's full detail — call https://www.dnd5eapi.co/api/2014/monsters/{index} and deserialize it into Monster, exactly like your test just did, except the JSON comes from an HTTP response body instead of a File

A few design questions worth settling before writing it:
Want to write DndApiClient.java together now, or do a checklist first like we did for Monster.java?Checklist first

Here's a checklist for DndApiClient.java, same format as the Monster.java one — what it needs to hold, what it needs to do, and what it should NOT do.
1. What it needs to hold

   A base URL constant — e.g. "https://www.dnd5eapi.co/api/2014" — so you're not hardcoding the full URL string in multiple places
   A reusable HttpClient instance (from java.net.http.HttpClient) — you generally want to create one and reuse it across calls, not build a new one per request
   A reusable Jackson ObjectMapper instance — same idea, one instance reused, since creating a new ObjectMapper per call is wasteful

2. What it needs to do

Fetch the monster list

    A method like List<MonsterSummary> fetchMonsterList() that calls GET /monsters
    This needs a new, smaller model class — MonsterSummary — since the list endpoint returns only {index, name, url} per entry, not a full stat block. Don't reuse Monster for this; it's a genuinely different (smaller) shape.
    The actual response wraps the array in an object ({ "count": N, "results": [...] }), so MonsterSummary fetching means deserializing into a small wrapper class, then pulling out .getResults()

Fetch one monster's full detail

    A method like Monster fetchMonsterDetail(String index) that calls GET /monsters/{index} and deserializes the response body straight into your existing Monster class — this is the exact same deserialization you already proved works, just fed by an HTTP response instead of a file

Handle the HTTP mechanics for both

    Build an HttpRequest with the right URL
    Send it via HttpClient.send()
    Check the response status code — a non-200 response (404, 500, etc.) shouldn't be treated as success; decide what happens then (throw an exception? return null? log and skip?)
    Pass the response body string to Jackson's ObjectMapper.readValue() for deserialization

3. Error handling responsibilities

   Network failures (IOException, timeouts) — decide whether this class catches and handles them, or lets them bubble up to whoever calls it (SyncService). Generally: let exceptions propagate up, and let the caller decide what to do (e.g. SyncService catches it and falls back to "we're offline, use local data")
   Malformed/unexpected JSON — same idea; a JsonProcessingException from Jackson should probably bubble up rather than being silently swallowed here

4. What this file should NOT do

   No SQL, no MonsterDao calls — this class only knows about the API, not your database
   No "is the app online" logic — that's ConnectivityChecker's job; DndApiClient just assumes it's allowed to try and lets failures speak for themselves
   No decision-making about which monsters to fetch or when to sync — that orchestration belongs to SyncService. This class just exposes "give me the list" and "give me monster X" as tools for that orchestrator to call.
   No UI-facing formatting/logic

One thing worth deciding before writing code: for fetchMonsterList(), do you want it to return the raw summaries (index/name/url only), or would it make more sense for this method to loop through and immediately call fetchMonsterDetail() for every single one, returning full Monster objects? I'd lean toward keeping them separate methods (list vs. detail) so SyncService can decide which monsters actually need full detail fetched (e.g. only new ones), rather than always fetching all ~300+ monsters' full data on every single sync. Want to lock that in as the approach, or talk through the trade-off more?