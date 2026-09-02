package com.kaitjie.bestiaryTest.model;

import com.kaitjie.bestiary.db.DatabaseManager;
import com.kaitjie.bestiary.db.MonsterDao;
import com.kaitjie.bestiary.model.*;

import java.util.List;

public class InsertFullMonsterTest {
    public static void main(String[] args) throws Exception {
        DatabaseManager dbManager = new DatabaseManager();
        MonsterDao monsterDao = new MonsterDao(dbManager);

        Monster testMonster = new Monster();
        testMonster.setIndex("test-goblin-3");
        testMonster.setName("Test Goblin 3");
        testMonster.setSize("Small");
        testMonster.setType("humanoid");
        testMonster.setChallengeRating(0.25);

        ArmorClass ac = new ArmorClass();
        ac.setType("natural");
        ac.setValue(15);
        testMonster.setArmorClass(List.of(ac));

        testMonster.setConditionImmunities(List.of("charmed", "frightened"));
        testMonster.setDamageImmunities(List.of("poison"));
        testMonster.setDamageResistances(List.of("cold"));
        testMonster.setDamageVulnerabilities(List.of("fire"));

        ApiRef profRef = new ApiRef();
        profRef.setIndex("skill-stealth");
        profRef.setName("Skill: Stealth");
        profRef.setUrl("/api/2014/proficiencies/skill-stealth");

        Proficiency prof = new Proficiency();
        prof.setValue(6);
        prof.setProficiency(profRef);
        testMonster.setProficiencies(List.of(prof));

        int monsterId = monsterDao.insertMonster(testMonster);
        System.out.println("Inserted monster with id: " + monsterId);

        monsterDao.insertArmorClasses(monsterId, testMonster);
        System.out.println("Armor classes inserted.");

        monsterDao.insertConditionImmunities(monsterId, testMonster);
        System.out.println("Condition immunities inserted.");

        monsterDao.insertDamageImmunities(monsterId, testMonster);
        System.out.println("Damage immunities inserted.");

        monsterDao.insertDamageResistance(monsterId, testMonster);
        System.out.println("Damage resistances inserted.");

        monsterDao.insertDamageVulnerabilities(monsterId, testMonster);
        System.out.println("Damage vulnerabilities inserted.");

        monsterDao.insertProficiencies(monsterId, testMonster);
        System.out.println("Proficiencies inserted.");

        dbManager.closeConnection();
        System.out.println("All done — check the database tables for real values.");
    }
}