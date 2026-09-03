package com.kaitjie.bestiaryTest.model;

import com.kaitjie.bestiary.api.DndApiClient;
import com.kaitjie.bestiary.db.DatabaseManager;
import com.kaitjie.bestiary.db.MonsterDao;
import com.kaitjie.bestiary.model.ActionEntry;
import com.kaitjie.bestiary.model.Monster;
import com.kaitjie.bestiary.model.SpecialAbility;

import java.util.List;

public class FullInsertTest {
    public static void main(String[] args) throws Exception {
        DndApiClient apiClient = new DndApiClient();
        DatabaseManager dbManager = new DatabaseManager();
        MonsterDao monsterDao = new MonsterDao(dbManager);

        Monster aboleth = apiClient.fetchMonsterDetail("aboleth");

        int monsterId = monsterDao.insertMonster(aboleth);
        System.out.println("Inserted monster id: " + monsterId);

        monsterDao.insertArmorClasses(monsterId, aboleth);
        monsterDao.insertProficiencies(monsterId, aboleth);
        monsterDao.insertDamageVulnerabilities(monsterId, aboleth);
        monsterDao.insertDamageResistance(monsterId, aboleth);
        monsterDao.insertDamageImmunities(monsterId, aboleth);
        monsterDao.insertConditionImmunities(monsterId, aboleth);
        System.out.println("Inserted core child tables.");

        List<SpecialAbility> abilities = aboleth.getSpecialAbilities();
        List<Integer> abilityIds = monsterDao.insertSpecialAbilities(monsterId, aboleth);
        for (int i = 0; i < abilities.size(); i++) {
            monsterDao.insertSpecialAbilityDamage(abilityIds.get(i), abilities.get(i));
        }
        System.out.println("Inserted special abilities + damage.");

        List<ActionEntry> actions = aboleth.getActions();
        List<Integer> actionIds = monsterDao.insertAction(monsterId, aboleth);
        for (int i = 0; i < actions.size(); i++) {
            monsterDao.insertActionDamage(actionIds.get(i), actions.get(i));
            monsterDao.insertSubAction(actionIds.get(i), actions.get(i));
        }
        System.out.println("Inserted actions + damage + sub-actions.");

        List<ActionEntry> legendaryActions = aboleth.getLegendaryActions();
        List<Integer> legendaryIds = monsterDao.insertLegendaryAction(monsterId, aboleth);
        for (int i = 0; i < legendaryActions.size(); i++) {
            monsterDao.insertLegendaryActionDamage(legendaryIds.get(i), legendaryActions.get(i));
        }
        System.out.println("Inserted legendary actions + damage.");

        dbManager.closeConnection();
        System.out.println("Full aboleth insert complete.");
    }
}