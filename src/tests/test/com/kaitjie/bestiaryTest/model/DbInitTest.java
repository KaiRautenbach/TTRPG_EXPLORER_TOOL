package com.kaitjie.bestiaryTest.model;

import com.kaitjie.bestiary.db.DatabaseManager;

public class DbInitTest {
    public static void main(String[] args) throws Exception {
        DatabaseManager dbManager = new DatabaseManager();
        System.out.println("Database initialized successfully.");
        dbManager.closeConnection();
    }
}