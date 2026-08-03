package za.co.wethincode;

import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;

public class Main {
    private static final Duration REFRESH_INTERVAL = Duration.ofHours(24);
    public static void main(String[] args) throws IOException {
        JsonLoader storage = new JsonLoader();

        if (storage.isStale(JsonLoader.DATA_FILE, REFRESH_INTERVAL)) {
            System.out.println("Data is missing or older than 24 hours — refreshing from API...");

            ApiReader reader = new ApiReader();
            String rawJson = reader.Reader();

            storage.writeToIndex(rawJson, JsonLoader.INDEX_FILE);
            storage.writeToMonster(JsonLoader.INDEX_FILE, JsonLoader.DATA_FILE);
        } else {
            System.out.println("Using cached data (last updated within 24 hours).");
        }
        while (true) {
            Scanner lineReader = new Scanner(System.in);
            System.out.print(">");
            String userInput = lineReader.nextLine();
            ResponseBuilder builder = new ResponseBuilder(userInput);
            System.out.println(builder.build());
        }
    }
}