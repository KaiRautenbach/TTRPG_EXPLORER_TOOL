package com.kaitjie.bestiary;

import com.kaitjie.bestiary.api.ConnectivityChecker;
import com.kaitjie.bestiary.api.DndApiClient;
import com.kaitjie.bestiary.db.DatabaseManager;
import com.kaitjie.bestiary.db.MonsterDao;
import com.kaitjie.bestiary.model.Monster;
import com.kaitjie.bestiary.sync.SyncService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            MonsterDao monsterDao = new MonsterDao(dbManager);
            DndApiClient apiClient = new DndApiClient();
            ConnectivityChecker connectivityChecker = new ConnectivityChecker();

            SyncService syncService = new SyncService(apiClient, monsterDao, connectivityChecker);

            System.out.println("Syncing bestiary...");
            syncService.syncMonsters();

            Scanner scanner = new Scanner(System.in);
            System.out.println("\nPocket Bestiary ready. Type a monster index (e.g. 'goblin') or 'quit' to exit.");

            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("quit")) {
                    break;
                }

                Monster monster = monsterDao.getMonsterByIndex(input);
                if (monster == null) {
                    System.out.println("No monster found with index '" + input + "'.");
                } else {
                    System.out.println(monster.getName() + " - HP: " + monster.getHitPoints()
                            + " - CR: " + monster.getChallengeRating());
                }
            }

            dbManager.closeConnection();
            System.out.println("Goodbye.");

        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }
}