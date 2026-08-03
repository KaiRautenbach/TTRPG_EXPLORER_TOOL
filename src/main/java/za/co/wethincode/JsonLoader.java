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
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.Instant;

public class JsonLoader {

    private static final String BASE_URL = "https://www.dnd5eapi.co";
    private final HttpClient client = HttpClient.newHttpClient();

    // ONE place that defines where the Resources folder lives.
    // Every read/write in this class uses this — no bare filenames anywhere else.
    private static final Path RESOURCES_DIR =
            Path.of(System.getProperty("user.dir"), "src", "main", "Resources");

    public static final Path INDEX_FILE = RESOURCES_DIR.resolve("Index.json");
    public static final Path DATA_FILE = RESOURCES_DIR.resolve("Data.json");

    public boolean isStale(Path filePath, Duration maxAge) {
        if (!Files.exists(filePath)) {
            return true;
        }

        try {
            FileTime lastModified = Files.getLastModifiedTime(filePath);
            Instant cutoff = Instant.now().minus(maxAge);
            return lastModified.toInstant().isBefore(cutoff);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
    }

    public void writeToIndex(String rawJson, Path filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            JsonElement parsed = JsonParser.parseString(rawJson);

            try (FileWriter writer = new FileWriter(filePath.toFile())) {
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

    public JsonArray writeToMonster(Path indexFilePath, Path outputFilePath) {
        JsonArray allMonsters = new JsonArray();

        try {
            String listJson = Files.readString(indexFilePath);
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
            System.out.println("Could not read index file: " + indexFilePath);
            e.printStackTrace();
            return allMonsters;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(outputFilePath.toFile())) {
            gson.toJson(allMonsters, writer);
            System.out.println("Saved to " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allMonsters;
    }
}