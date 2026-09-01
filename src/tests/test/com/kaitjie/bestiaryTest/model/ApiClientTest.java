package com.kaitjie.bestiaryTest.model;

import com.kaitjie.bestiary.api.DndApiClient;
import com.kaitjie.bestiary.model.Monster;
import com.kaitjie.bestiary.model.MonsterSummary;

import java.util.List;

public class ApiClientTest {
    public static void main(String[] args) throws Exception {
        DndApiClient client = new DndApiClient();

        List<MonsterSummary> all = client.fetchMonsterList();
        System.out.println("Total monsters: " + all.size());
        System.out.println("First one: " + all.get(0).getName() + " (" + all.get(0).getIndex() + ")");

        Monster goblin = client.fetchMonsterDetail("goblin");
        System.out.println("Fetched: " + goblin.getName() + " - HP: " + goblin.getHitPoints() + " - AC: " + goblin.getArmorClass().get(0).getValue());
    }
}