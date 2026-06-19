package za.co.wethincode;
import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonLoader {

    private static final String BASE_URL = "https://www.dnd5eapi.co";
    private final HttpClient client = HttpClient.newHttpClient();

    public void writeToIndex(String rawJson, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            JsonElement parsed = JsonParser.parseString(rawJson);

            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(parsed, writer);
                System.out.println("Saved to " + filePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String fetchRaw(String fullUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // CHANGED: takes the INPUT list path and the OUTPUT path as two separate parameters
    public JsonArray writeToMonster(String indexFilePath, String outputFilePath) {
        JsonArray allMonsters = new JsonArray();

        try {
            // CHANGED: uses the parameter instead of a hardcoded filename
            String listJson = Files.readString(Path.of(indexFilePath));
            JsonObject listObject = JsonParser.parseString(listJson).getAsJsonObject();
            JsonArray results = listObject.getAsJsonArray("results");

            for (JsonElement entry : results) {
                String relativeUrl = entry.getAsJsonObject().get("url").getAsString();
                String fullUrl = BASE_URL + relativeUrl;

                try {
                    String monsterJson = fetchRaw(fullUrl);
                    allMonsters.add(JsonParser.parseString(monsterJson));
                    System.out.println("Fetched: " + relativeUrl);
                } catch (Exception e) {
                    System.out.println("Failed to fetch: " + relativeUrl + " — " + e.getMessage());
                }
            }

        } catch (IOException e) {
            // This is the NoSuchFileException case — the index file itself was missing/unreadable
            System.out.println("Could not read index file: " + indexFilePath);
            e.printStackTrace();
            return allMonsters; // bail out early, nothing to write
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ADDED: actually write the accumulated array out, using outputFilePath
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            gson.toJson(allMonsters, writer);
            System.out.println("Saved to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allMonsters;
    }
}