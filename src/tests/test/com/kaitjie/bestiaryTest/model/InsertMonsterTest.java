package com.kaitjie.bestiaryTest.model;

import com.kaitjie.bestiary.db.DatabaseManager;
import com.kaitjie.bestiary.db.MonsterDao;
import com.kaitjie.bestiary.model.Monster;
import com.kaitjie.bestiary.model.Senses;
import com.kaitjie.bestiary.model.Speed;

public class InsertMonsterTest {
    public static void main(String[] args) throws Exception {
        DatabaseManager dbManager = new DatabaseManager();
        MonsterDao monsterDao = new MonsterDao(dbManager);

        Monster testMonster = new Monster();
        testMonster.setIndex("test-goblin-2");
        testMonster.setName("Test Goblin 2");
        testMonster.setSize("Small");
        testMonster.setType("humanoid");
        testMonster.setAlignment("neutral evil");
        testMonster.setHitPoints(7);
        testMonster.setHitDice("2d6");
        testMonster.setStrength(8);
        testMonster.setDexterity(14);
        testMonster.setConstitution(10);
        testMonster.setIntelligence(10);
        testMonster.setWisdom(8);
        testMonster.setCharisma(8);
        testMonster.setLanguages("Common, Goblin");
        testMonster.setChallengeRating(0.25);
        testMonster.setProficiencyBonus(2);
        testMonster.setXp(50);

        Speed speed = new Speed();
        speed.setWalk("30 ft.");
        testMonster.setSpeed(speed);

        Senses senses = new Senses();
        senses.setDarkVision("60 ft.");
        senses.setPassivePerception(9);
        testMonster.setSenses(senses);

        int generatedId = monsterDao.insertMonster(testMonster);

        System.out.println("Inserted monster with id: " + generatedId);

        dbManager.closeConnection();
    }
}