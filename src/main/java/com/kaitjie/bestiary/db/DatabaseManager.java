package com.kaitjie.bestiary.db;

// imports will go here:
import org.sqlite.SQLiteConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    // ---- FIELDS ----
    // constant: database file name, e.g. "bestiary.db"
    private static final String DB_FILE = "bestiary.db";
    // constant: JDBC connection URL, e.g. "jdbc:sqlite:" + file name
    private static final String DB_URL = "jdbc:sqlite:" + DB_FILE;
    // field: the shared Connection object
    private final Connection connection;


    // ---- CONSTRUCTOR ----
    public DatabaseManager() throws SQLException {
        // open the connection (assign to the field)
        connection = DriverManager.getConnection(DB_URL);
        // call the schema-init method so tables exist before anything else runs
        initializeSchema();
    }




    // ---- CONNECTION ACCESS ----
    // public method: getConnection()
    public Connection getConnection() {
        return connection;
    }
    // returns the shared Connection field
    // this is what MonsterDao will call to get access to the database


    // ---- SCHEMA INITIALIZATION ----
    // private method: initializeSchema() (or createTables(), your naming)
    private void initializeSchema() throws SQLException {
        // Step 1: get a Statement object from the connection
        // this is the object you use to actually "send" SQL commands
        Statement stmt = connection.createStatement();

        // Step 2: execute one CREATE TABLE IF NOT EXISTS per table,
        // in parent-before-child order (matching your ERD)

        // ---- 1. monsters ---- \\
        String monsters = """
                CREATE TABLE IF NOT EXISTS monsters(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    monster_index TEXT UNIQUE NOT NULL,
                    name TEXT NOT NULL,
                    size TEXT,
                    type TEXT,
                    alignment TEXT,
                    hit_points INTEGER,
                    hit_dice TEXT,
                    hit_points_roll TEXT,
                    strength INTEGER,
                    dexterity INTEGER,
                    constitution INTEGER,
                    intelligence INTEGER,
                    wisdom INTEGER,
                    charisma INTEGER,
                    language TEXT,
                    challenge_rating REAL,
                    proficiency_bonus INTEGER,
                    xp INTEGER,
                    image TEXT,
                    url TEXT,
                    last_updated TEXT,
                    speed_walk TEXT,
                    speed_fly TEXT,
                    speed_swim TEXT,
                    speed_climb TEXT,
                    speed_burrow TEXT,
                    speed_hover INTEGER,
                    darkvision TEXT,
                    blindsight TEXT,
                    tremorsense TEXT,
                    truesight TEXT,
                    passive_perception INTEGER
                    );
                """;
        stmt.execute(monsters);

        // ---- 2. armor_classes ---- \\

        String armor_classes = """
                CREATE TABLE IF NOT EXISTS armor_classes(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    monster_id INTEGER NOT NULL REFERENCES monsters(id),
                    type TEXT,
                    value INTEGER
                    );
               """;
        stmt.execute(armor_classes);

        // ---- 3. proficiencies ---- \\
        String proficiencies = """
                CREATE TABLE IF NOT EXISTS proficiencies(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    monster_id INTEGER NOT NULL REFERENCES monsters(id),
                    value INTEGER,
                    prof_index TEXT,
                    prof_name TEXT,
                    prof_url TEXT
                    )
                """;
        stmt.execute(proficiencies);

        // ---- 4. damage_vulnerabilities ---- \\
        String damage_vulnerabilities = """
                
                CREATE TABLE IF NOT EXISTS damage_vulnerabilities(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    monster_id INTEGER NOT NULL REFERENCES monsters(id),
                    value TEXT
                    )
                """;
        stmt.execute(damage_vulnerabilities);

        // ---- 5. damage_resistances ---- \\
        String damage_resistances = """
                CREATE TABLE IF NOT EXISTS damage_resistances(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    monster_id INTEGER NOT NULL REFERENCES monsters(id),
                    value TEXT
                    )
                """;
        stmt.execute(damage_resistances);

        // ---- 6. damage_immunities ---- \\
        String damage_immunities = """
                CREATE TABLE IF NOT EXISTS damage_immunities(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    monster_id INTEGER NOT NULL REFERENCES monsters(id),
                    value TEXT
                    )
                """;
        stmt.execute(damage_immunities);

        // ---- 7. condition_immunities ---- \\
        String condition_immunities = """
                CREATE TABLE IF NOT EXISTS condition_immunities(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    monster_id INTEGER NOT NULL REFERENCES monsters(id),
                    value TEXT
                    )
                """;
        stmt.execute(condition_immunities);

        // ---- 8. special_abilities ---- \\
        String special_abilities = """
                CREATE TABLE IF NOT EXISTS special_abilities(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    monster_id INTEGER NOT NULL REFERENCES monsters(id),
                    name TEXT,
                    desc TEXT,
                    dc_type TEXT,
                    dc_value INTEGER,
                    success_type TEXT,
                    usage_type TEXT,
                    usage_times INTEGER
                    )
                """;
        stmt.execute(special_abilities);

        // ---- 9. special_ability_damage ---- \\
        String special_ability_damage = """
                CREATE TABLE IF NOT EXISTS special_abilities_damage(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    special_ability_id INTEGER NOT NULL REFERENCES special_abilities(id),
                    damage_type TEXT,
                    damage_dice TEXT
                    )
                """;
        stmt.execute(special_ability_damage);

        // ---- 10. actions ---- \\
        String actions = """
                CREATE TABLE IF NOT EXISTS actions(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    monster_id INTEGER NOT NULL REFERENCES monsters(id),
                    name TEXT,
                    desc TEXT,
                    attack_bonus INTEGER,
                    multiattack_type TEXT,
                    dc_type TEXT,
                    dc_value INTEGER,
                    success_type TEXT,
                    usage_type TEXT,
                    usage_times INTEGER
                    )
                """;
        stmt.execute(actions);

        // ---- 11. action_damage ---- \\
        String action_damage = """
                CREATE TABLE IF NOT EXISTS action_damage(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    action_id INTEGER NOT NULL REFERENCES actions(id),
                    damage_type TEXT,
                    damage_dice TEXT
                    )
                """;
        stmt.execute(action_damage);

        // ---- 12. sub_actions ---- \\
        String sub_actions = """
                CREATE TABLE IF NOT EXISTS sub_actions(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    action_id INTEGER NOT NULL REFERENCES actions(id),
                    action_name TEXT,
                    count TEXT,
                    type TEXT
                    )
                """;
        stmt.execute(sub_actions);

        // ---- 13. legendary_actions ---- \\
        String legendary_actions = """
                CREATE TABLE IF NOT EXISTS legendary_actions(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    monster_id INTEGER NOT NULL REFERENCES monsters(id),
                    name TEXT,
                    desc TEXT,
                    attack_bonus INTEGER,
                    dc_type TEXT,
                    dc_value INTEGER,
                    success_type TEXT,
                    usage_type TEXT,
                    usage_times INTEGER
                    )
                """;
        stmt.execute(legendary_actions);

        // ---- 14. legendary_action_damage ---- \\
        String legendary_action_damage = """
                CREATE TABLE IF NOT EXISTS legendary_action_damage(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    legendary_action_id INTEGER NOT NULL REFERENCES legendary_action(id),
                    damage_type TEXT,
                    damage_dice TEXT
                    )
                """;
        stmt.execute(legendary_action_damage);
    }



    // ---- CLEANUP ----
    // public method: closeConnection()
    // closes the Connection field cleanly
}