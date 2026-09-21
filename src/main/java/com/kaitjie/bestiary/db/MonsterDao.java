package com.kaitjie.bestiary.db;

import com.kaitjie.bestiary.model.*;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonsterDao {

    private final Connection connection;

    public MonsterDao(DatabaseManager dbManager) {
        this.connection = dbManager.getConnection();
    }

    public void saveFullMonster(Monster monster) throws SQLException {

        try {
            connection.setAutoCommit(false);

            int monsterId = insertMonster(monster);

            // ---- simple child tables ----
            if (monster.getArmorClass() != null) {
                insertArmorClasses(monsterId, monster);
            }
            if (monster.getProficiencies() != null) {
                insertProficiencies(monsterId, monster);
            }
            if (monster.getDamageVulnerabilities() != null) {
                insertDamageVulnerabilities(monsterId, monster);
            }
            if (monster.getDamageResistances() != null) {
                insertDamageResistance(monsterId, monster);
            }
            if (monster.getDamageImmunities() != null) {
                insertDamageImmunities(monsterId, monster);
            }
            if (monster.getConditionImmunities() != null) {
                insertConditionImmunities(monsterId, monster);
            }

            // ---- special abilities + their damage ----
            List<SpecialAbility> abilities = monster.getSpecialAbilities();
            if (abilities != null) {
                List<Integer> abilityIds = insertSpecialAbilities(monsterId, monster);
                for (int i = 0; i < abilities.size(); i++) {
                    SpecialAbility ability = abilities.get(i);
                    if (ability.getDamage() != null) {
                        insertSpecialAbilityDamage(abilityIds.get(i), ability);
                    }
                }
            }

            // ---- actions + damage + sub-actions ----
            List<ActionEntry> actions = monster.getActions();
            if (actions != null) {
                List<Integer> actionIds = insertAction(monsterId, monster);
                for (int i = 0; i < actions.size(); i++) {
                    ActionEntry action = actions.get(i);
                    int actionId = actionIds.get(i);

                    if (action.getDamage() != null) {
                        insertActionDamage(actionId, action);
                    }
                    if (action.getActions() != null) {
                        insertSubAction(actionId, action);
                    }
                }
            }

            // ---- legendary actions + damage ----
            List<ActionEntry> legendaryActions = monster.getLegendaryActions();
            if (legendaryActions != null) {
                List<Integer> legendaryIds = insertLegendaryAction(monsterId, monster);
                for (int i = 0; i < legendaryActions.size(); i++) {
                    ActionEntry lAction = legendaryActions.get(i);
                    if (lAction.getDamage() != null) {
                        insertLegendaryActionDamage(legendaryIds.get(i), lAction);
                    }
                }
            }

            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
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

    public List<Integer> insertAction(int monsterId, Monster monster) throws SQLException{
        List<ActionEntry> actions = monster.getActions();
        List<Integer> actionIds = new ArrayList<>();

        String sql = "INSERT INTO actions " +
                "(monster_id,name,desc,attack_bonus," +
                "multiattack_type,dc_type,dc_value," +
                "success_type,usage_type,usage_times) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";
        for (ActionEntry action : actions){
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            Dc dc = action.getDc();
            Usage usage = action.getUsage();
            Integer attackBonus = action.getAttackBonus();

            ps.setInt(1,monsterId);
            ps.setString(2,action.getName());
            ps.setString(3,action.getDesc());

            if (attackBonus != null) {
                ps.setInt(4, attackBonus);
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setString(5, action.getMultiattackType());

            if (dc != null) {
                ps.setString(6, dc.getDcType().getName());
                ps.setInt(7, dc.getDcValue());
                ps.setString(8, dc.getSuccessType());
            } else {
                ps.setString(6, null);
                ps.setInt(7, 0);
                ps.setString(8, null);
            }

            if (usage != null) {
                ps.setString(9, usage.getType());
                ps.setInt(10, usage.getTimes());
            } else {
                ps.setString(9, null);
                ps.setInt(10, 0);
            }
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                actionIds.add(rs.getInt(1));
            } else {
                throw new SQLException("Insert succeeded but no generated key was returned.");
            }
        }
        return actionIds;
    }

    public void insertActionDamage(int actionId, ActionEntry action) throws SQLException{

        String sql = "INSERT INTO action_damage (action_id, damage_type, damage_dice) VALUES (?,?,?)";

        for (Damage d : action.getDamage()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, actionId);
            ps.setString(2, d.getDamageType().getName());
            ps.setString(3, d.getDamageDice());
            ps.executeUpdate();
        }
    }

    public void insertSubAction(int actionId, ActionEntry action) throws SQLException{

        String sql = "INSERT INTO sub_actions " +
                "(action_id,action_name,count,type) " +
                "VALUES(?,?,?,?)";

        for (SubAction subAction : action.getActions()){
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,actionId);
            ps.setString(2,subAction.getActionName());
            ps.setString(3,subAction.getCount());
            ps.setString(4, subAction.getType());
            ps.executeUpdate();
        }
    }

    public List<Integer> insertSpecialAbilities(int monsterId,Monster monster) throws SQLException{
        List<SpecialAbility> specialAbilities = monster.getSpecialAbilities();
        List<Integer> abilityIds = new ArrayList<>();

        String sql = "INSERT INTO special_abilities (monster_id,name,desc,dc_type,dc_value,success_type,usage_type,usage_times) VALUES (?,?,?,?,?,?,?,?)";

        for (SpecialAbility sp : specialAbilities){
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            Dc dc = sp.getDc();
            Usage usage = sp.getUsage();

            ps.setInt(1,monsterId);
            ps.setString(2,sp.getName());
            ps.setString(3,sp.getDesc());

            if (dc != null) {
                ps.setString(4, dc.getDcType().getName());
                ps.setInt(5, dc.getDcValue());
                ps.setString(6, dc.getSuccessType());
            } else {
                ps.setString(4, null);
                ps.setInt(5, 0);
                ps.setString(6, null);
            }

            if (usage != null) {
                ps.setString(7, usage.getType());
                ps.setInt(8, usage.getTimes());
            } else {
                ps.setString(7, null);
                ps.setInt(8, 0);
            }
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                abilityIds.add(rs.getInt(1));
            } else {
                throw new SQLException("Insert succeeded but no generated key was returned.");
            }
        }
        return abilityIds;
    }

    public void insertSpecialAbilityDamage(int abilityId, SpecialAbility ability) throws SQLException{
        String sql = "INSERT INTO special_ability_damage (special_ability_id, damage_type, damage_dice) VALUES (?,?,?)";

        for (Damage d : ability.getDamage()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, abilityId);
            ps.setString(2, d.getDamageType().getName());
            ps.setString(3, d.getDamageDice());
            ps.executeUpdate();
        }
    }

    public List<Integer> insertLegendaryAction(int monsterId, Monster monster) throws SQLException{
        List<ActionEntry> legendaryActions = monster.getLegendaryActions();
        List<Integer> lActionIds = new ArrayList<>();

        String sql = "INSERT INTO legendary_actions " +
                "(monster_id,name,desc,attack_bonus," +
                "dc_type,dc_value," +
                "success_type,usage_type,usage_times) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";

        for (ActionEntry lAction : legendaryActions){
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            Dc dc = lAction.getDc();
            Usage usage = lAction.getUsage();
            Integer attackBonus = lAction.getAttackBonus();

            ps.setInt(1,monsterId);
            ps.setString(2,lAction.getName());
            ps.setString(3,lAction.getDesc());

            if (attackBonus != null) {
                ps.setInt(4, attackBonus);
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            if (dc != null) {
                ps.setString(5, dc.getDcType().getName());
                ps.setInt(6, dc.getDcValue());
                ps.setString(7, dc.getSuccessType());
            } else {
                ps.setString(5, null);
                ps.setInt(6, 0);
                ps.setString(7, null);
            }

            if (usage != null) {
                ps.setString(8, usage.getType());
                ps.setInt(9, usage.getTimes());
            } else {
                ps.setString(8, null);
                ps.setInt(9, 0);
            }
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                lActionIds.add(rs.getInt(1));
            } else {
                throw new SQLException("Insert succeeded but no generated key was returned.");
            }
        }
        return lActionIds;
    }

    public void insertLegendaryActionDamage(int lActionId, ActionEntry lAction) throws SQLException{
        String sql = "INSERT INTO legendary_action_damage (legendary_action_id, damage_type, damage_dice) VALUES (?,?,?)";

        for (Damage d : lAction.getDamage()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, lActionId);
            ps.setString(2, d.getDamageType().getName());
            ps.setString(3, d.getDamageDice());
            ps.executeUpdate();
        }
    }

    public Monster getMonsterByIndex(String index) throws SQLException {

        // ---- STEP 1: fetch the core monster row ----
        // build SELECT * FROM monsters WHERE monster_index = ?
        // prepareStatement, set the index param, executeQuery()
        // if no row found (rs.next() is false): decide what to return (null? throw?)
        // if found: create a new Monster object, read every column by name,
        //           call the matching setX() for each
        // capture the row's "id" column into a local variable — you'll need it
        // for every query below
        String sql = "SELECT * FROM monsters WHERE monster_index = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,index);

        ResultSet rs = ps.executeQuery();

        if (rs.next()){
            int ref = rs.getInt("id");
            Monster monster = new Monster();
            monster.setIndex(rs.getString("monster_index"));
            monster.setName(rs.getString("name"));
            monster.setSize(rs.getString("size"));
            monster.setType(rs.getString("type"));
            monster.setAlignment(rs.getString("alignment"));
            monster.setHitPoints(rs.getInt("hit_points"));
            monster.setHitDice(rs.getString("hit_dice"));
            monster.setHitPointsRoll(rs.getString("hit_points_roll"));
            monster.setStrength(rs.getInt("strength"));
            monster.setDexterity(rs.getInt("dexterity"));
            monster.setConstitution(rs.getInt("constitution"));
            monster.setIntelligence(rs.getInt("intelligence"));
            monster.setWisdom(rs.getInt("wisdom"));
            monster.setCharisma(rs.getInt("charisma"));
            monster.setLanguages(rs.getString("languages"));
            monster.setChallengeRating(rs.getDouble("challenge_rating"));
            monster.setProficiencyBonus(rs.getInt("proficiency_bonus"));
            monster.setXp(rs.getInt("xp"));
            monster.setImage(rs.getString("image"));
            monster.setUrl(rs.getString("url"));
            monster.setLastUpdated(rs.getString("last_updated"));

            Speed speed = new Speed();
            speed.setWalk(rs.getString("speed_walk"));
            speed.setFly(rs.getString("speed_fly"));
            speed.setSwim(rs.getString("speed_swim"));
            speed.setClimb(rs.getString("speed_climb"));
            speed.setBurrow(rs.getString("speed_burrow"));
            speed.setHover(rs.getInt("speed_hover") == 1);
            monster.setSpeed(speed);

            Senses senses = new Senses();
            senses.setBlindSight(rs.getString("blindsight"));
            senses.setTremorSense(rs.getString("tremorsense"));
            senses.setDarkVision(rs.getString("darkvision"));
            senses.setTrueSight(rs.getString("truesight"));
            senses.setPassivePerception(rs.getInt("passive_perception"));
            monster.setSenses(senses);

            return monster;
        }else{
            return null;
        }

        // ---- STEP 2: simple one-to-many tables (no nested objects) ----
        // for each of: armor_classes, proficiencies,
        //              damage_vulnerabilities, damage_resistances,
        //              damage_immunities, condition_immunities
        //   - SELECT * FROM <table> WHERE monster_id = ?
        //   - while (rs.next()): build the object (ArmorClass / Proficiency / String),
        //     add to a List
        //   - call monster.setX(thatList)


        // ---- STEP 3: special_abilities (has a nested damage list) ----
        // SELECT * FROM special_abilities WHERE monster_id = ?
        // while (rs.next()):
        //   - build a SpecialAbility from this row
        //   - capture this row's "id" (the special_ability's own id)
        //   - run a SECOND query: SELECT * FROM special_ability_damage
        //     WHERE special_ability_id = ?
        //   - while (rs2.next()): build Damage objects, add to a list
        //   - attach that damage list to the SpecialAbility
        //   - add the finished SpecialAbility to an outer list
        // monster.setSpecialAbilities(thatOuterList)


        // ---- STEP 4: actions (has TWO nested lists: damage AND sub_actions) ----
        // same shape as Step 3, but for each action row, run two extra
        // nested queries instead of one:
        //   - action_damage WHERE action_id = ?
        //   - sub_actions WHERE action_id = ?
        // monster.setActions(thatOuterList)


        // ---- STEP 5: legendary_actions (same shape as special_abilities) ----
        // SELECT * FROM legendary_actions WHERE monster_id = ?
        // nested query: legendary_action_damage WHERE legendary_action_id = ?
        // monster.setLegendaryActions(thatOuterList)


        // ---- STEP 6: return the fully assembled monster ----
        // return monster;
    }
    public List<String> getAllMonsterIndexes() throws SQLException {
        List<String> indexes = new ArrayList<>();

        String sql = "SELECT monster_index FROM monsters";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            indexes.add(rs.getString("monster_index"));
        }

        return indexes;
    }
}


