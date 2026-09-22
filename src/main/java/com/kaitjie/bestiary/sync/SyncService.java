package com.kaitjie.bestiary.sync;

import com.kaitjie.bestiary.api.ConnectivityChecker;
import com.kaitjie.bestiary.api.DndApiClient;
import com.kaitjie.bestiary.db.MonsterDao;
import com.kaitjie.bestiary.model.Monster;
import com.kaitjie.bestiary.model.MonsterSummary;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SyncService {

    private final DndApiClient apiClient;
    private final MonsterDao monsterDao;
    private final ConnectivityChecker connectivityChecker;

    public SyncService(DndApiClient apiClient, MonsterDao monsterDao, ConnectivityChecker connectivityChecker) {
        this.apiClient = apiClient;
        this.monsterDao = monsterDao;
        this.connectivityChecker = connectivityChecker;
    }

    public void syncMonsters() throws SQLException, IOException, InterruptedException {

        if (!connectivityChecker.isOnline()) {
            System.out.println("Offline — skipping sync.");
            return;
        }

        List<MonsterSummary> remoteList = apiClient.fetchMonsterList();
        List<String> localIndexes = monsterDao.getAllMonsterIndexes();

        List<String> missingIndexes = new ArrayList<>();
        for (MonsterSummary summary : remoteList) {
            if (!localIndexes.contains(summary.getIndex())) {
                missingIndexes.add(summary.getIndex());
            }
        }

        System.out.println("Found " + missingIndexes.size() + " new monsters to sync.");

        List<Monster> monstersToSave = new ArrayList<>();
        for (String index : missingIndexes) {
            Monster monster = apiClient.fetchMonsterDetail(index);
            monstersToSave.add(monster);
            System.out.println("Fetched: " + index);
        }

        for (Monster monster : monstersToSave) {
            monsterDao.saveFullMonster(monster);
            System.out.println("Saved: " + monster.getIndex());
        }

        System.out.println("Sync complete. " + monstersToSave.size() + " monsters saved.");
    }
}