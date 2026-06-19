package za.co.wethincode;

import java.util.Scanner;

public class Main {
    public static  void main(String[] args) {
        ApiReader reader = new ApiReader();
        String rawJson = reader.Reader();

        JsonLoader storage = new JsonLoader();
        storage.writeToIndex(rawJson, "Index.json");

        storage.writeToMonster("Index.json", "Data.json");
    }
}
