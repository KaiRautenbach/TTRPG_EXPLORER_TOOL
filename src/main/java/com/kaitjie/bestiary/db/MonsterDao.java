package com.kaitjie.bestiary.db;

import com.kaitjie.bestiary.model.*;

import java.sql.*;

public class MonsterDao {

    private final Connection connection;

    public MonsterDao(DatabaseManager dbManager) {
        this.connection = dbManager.getConnection();
    }

    public int insertMonster(Monster monster) throws SQLException {
        Speed speed = monster.getSpeed();
        Senses senses = monster.getSenses();

        String sql = "INSERT INTO monsters " +
                "(monster_index,name,size,type," +
                "alignment,hit_points,hit_dice,hit_points_roll,strength," +
                "dexterity,constitution,intelligence,wisdom,charisma," +
                "languages,challenge_rating,proficiency_bonus,xp,image," +
                "url,last_updated,speed_walk,speed_fly,speed_swim," +
                "speed_climb,speed_burrow,speed_hover,darkvision,blindsight," +
                "tremorsense,truesight,passive_perception) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, monster.getIndex());
        ps.setString(2, monster.getName());
        ps.setString(3, monster.getSize());
        ps.setString(4, monster.getType());
        ps.setString(5, monster.getAlignment());
        ps.setInt(6, monster.getHitPoints());
        ps.setString(7, monster.getHitDice());
        ps.setString(8, monster.getHitPointsRoll());
        ps.setInt(9, monster.getStrength());
        ps.setInt(10, monster.getDexterity());
        ps.setInt(11, monster.getConstitution());
        ps.setInt(12, monster.getIntelligence());
        ps.setInt(13, monster.getWisdom());
        ps.setInt(14, monster.getCharisma());
        ps.setString(15, monster.getLanguages());
        ps.setDouble(16, monster.getChallengeRating());
        ps.setInt(17, monster.getProficiencyBonus());
        ps.setInt(18, monster.getXp());
        ps.setString(19, monster.getImage());
        ps.setString(20, monster.getUrl());
        ps.setString(21, monster.getLastUpdated());

        if (speed != null) {
            ps.setString(22, speed.getWalk());
            ps.setString(23, speed.getFly());
            ps.setString(24, speed.getSwim());
            ps.setString(25, speed.getClimb());
            ps.setString(26, speed.getBurrow());

            Boolean hover = speed.getHover();
            if (hover != null && hover) {
                ps.setInt(27, 1);
            } else {
                ps.setInt(27, 0);
            }
        } else {
            ps.setString(22, null);
            ps.setString(23, null);
            ps.setString(24, null);
            ps.setString(25, null);
            ps.setString(26, null);
            ps.setInt(27, 0);
        }

        if (senses != null) {
            ps.setString(28, senses.getDarkVision());
            ps.setString(29, senses.getBlindSight());
            ps.setString(30, senses.getTremorSense());
            ps.setString(31, senses.getTrueSight());
            ps.setInt(32, monster.getSenses().getPassivePerception());
        } else{
            ps.setString(28, null);
            ps.setString(29, null);
            ps.setString(30, null);
            ps.setString(31, null);
            ps.setInt(32, 0);
        }
        ps.executeUpdate();

        ResultSet keys = ps.getGeneratedKeys();

        if (keys.next()) {
            return keys.getInt(1);
        }
        throw new SQLException("Insert succeeded but no generated key was returned.");
    }

    public void insertArmorClasses(int monsterId,Monster monster) throws SQLException {

        String sql = "INSERT INTO armor_classes" +
                "(monster_id, type, value) " +
                "VALUES (?,?,?)";

        for (ArmorClass ac : monster.getArmorClass()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, monsterId);
            ps.setString(2, ac.getType());
            ps.setInt(3, ac.getValue());
            ps.executeUpdate();
        }
    }

    public void insertConditionImmunities(int monsterId, Monster monster) throws SQLException {

        String sql = "INSERT INTO condition_immunities(monster_id, value) VALUES (?,?)";

        for (String condition : monster.getConditionImmunities()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,monsterId);
            ps.setString(2,condition);
            ps.executeUpdate();
        }
    }

    public void insertDamageImmunities(int monsterId, Monster monster) throws SQLException{

        String sql = "INSERT INTO damage_immunities (monster_id,value) VALUES (?,?)";

        for (String immunities : monster.getDamageImmunities()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,monsterId);
            ps.setString(2,immunities);
            ps.executeUpdate();
        }
    }

    public void insertDamageResistance(int monsterId, Monster monster) throws SQLException {

        String sql = "INSERT INTO damage_resistances (monster_id, value) VALUES (?,?)";

        for (String resistances : monster.getDamageResistances()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,monsterId);
            ps.setString(2,resistances);
            ps.executeUpdate();
        }
    }

    public void insertDamageVulnerabilities(int monsterId, Monster monster) throws SQLException{

        String sql = "INSERT INTO damage_vulnerabilities (monster_id, value) VALUES (?,?)";

        for (String vulnerabilities : monster.getDamageVulnerabilities()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, monsterId);
            ps.setString(2,vulnerabilities);
            ps.executeUpdate();
        }
    }

    public void insertProficiencies(int monsterId, Monster monster) throws SQLException{

        String sql = "INSERT INTO proficiencies (monster_id, value, prof_index, prof_name, prof_url) VALUES (?,?,?,?,?)";

        for (Proficiency prof : monster.getProficiencies()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, monsterId);
            ps.setInt(2,prof.getValue());
            ps.setString(3, prof.getProficiency().getIndex());
            ps.setString(4,prof.getProficiency().getName());
            ps.setString(5,prof.getProficiency().getUrl());
            ps.executeUpdate();
        }
    }
}
