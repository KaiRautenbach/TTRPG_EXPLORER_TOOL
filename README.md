# Pocket Bestiary

## File structure

```
pocket-bestiary/
├── pom.xml
├── src/main/java/com/yourname/bestiary/
│   ├── Main.java
│   ├── model/
│   │   ├── Monster.java
│   │   ├── MonsterSummary.java
│   │   ├── MonsterListResponse.java
│   │   ├── ArmorClass.java
│   │   ├── Speed.java
│   │   ├── Senses.java
│   │   ├── Proficiency.java
│   │   ├── ApiRef.java
│   │   ├── SpecialAbility.java
│   │   ├── ActionEntry.java
│   │   ├── SubAction.java
│   │   ├── Damage.java
│   │   ├── Dc.java
│   │   └── Usage.java
│   ├── api/
│   │   ├── DndApiClient.java      // HTTP calls to dnd5eapi.co
│   │   └── ConnectivityChecker.java
│   ├── db/
│   │   ├── DatabaseManager.java   // connection, schema init
│   │   └── MonsterDao.java        // insert/update/query
│   ├── sync/
│   │   └── SyncService.java       // orchestrates: check online -> fetch -> diff -> upsert
│   └── ui/
│       └── MainController.java    // (once you're on JavaFX)
└── bestiary.db                    // created at runtime
```

## Database schema

### monsters
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | auto-increment |
| monster_index | TEXT | unique, e.g. "goblin" |
| name | TEXT | |
| size | TEXT | |
| type | TEXT | |
| alignment | TEXT | |
| hit_points | INTEGER | |
| hit_dice | TEXT | |
| hit_points_roll | TEXT | |
| strength | INTEGER | |
| dexterity | INTEGER | |
| constitution | INTEGER | |
| intelligence | INTEGER | |
| wisdom | INTEGER | |
| charisma | INTEGER | |
| languages | TEXT | |
| challenge_rating | REAL | |
| proficiency_bonus | INTEGER | |
| xp | INTEGER | |
| image | TEXT | |
| url | TEXT | |
| last_updated | TEXT | |
| speed_walk | TEXT | |
| speed_fly | TEXT | |
| speed_swim | TEXT | |
| speed_climb | TEXT | |
| speed_burrow | TEXT | |
| speed_hover | INTEGER | 0/1 |
| darkvision | TEXT | |
| blindsight | TEXT | |
| tremorsense | TEXT | |
| truesight | TEXT | |
| passive_perception | INTEGER | |

### armor_classes
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| monster_id | INTEGER (FK) | → monsters.id |
| type | TEXT | |
| value | INTEGER | |

### proficiencies
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| monster_id | INTEGER (FK) | → monsters.id |
| value | INTEGER | |
| prof_index | TEXT | |
| prof_name | TEXT | |
| prof_url | TEXT | |

### damage_vulnerabilities
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| monster_id | INTEGER (FK) | → monsters.id |
| value | TEXT | |

### damage_resistances
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| monster_id | INTEGER (FK) | → monsters.id |
| value | TEXT | |

### damage_immunities
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| monster_id | INTEGER (FK) | → monsters.id |
| value | TEXT | |

### condition_immunities
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| monster_id | INTEGER (FK) | → monsters.id |
| value | TEXT | |

### special_abilities
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| monster_id | INTEGER (FK) | → monsters.id |
| name | TEXT | |
| desc | TEXT | |
| dc_type | TEXT | |
| dc_value | INTEGER | |
| success_type | TEXT | |
| usage_type | TEXT | |
| usage_times | INTEGER | |

### special_ability_damage
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| special_ability_id | INTEGER (FK) | → special_abilities.id |
| damage_type | TEXT | |
| damage_dice | TEXT | |

### actions
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| monster_id | INTEGER (FK) | → monsters.id |
| name | TEXT | |
| desc | TEXT | |
| attack_bonus | INTEGER | |
| multiattack_type | TEXT | |
| dc_type | TEXT | |
| dc_value | INTEGER | |
| success_type | TEXT | |
| usage_type | TEXT | |
| usage_times | INTEGER | |

### action_damage
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| action_id | INTEGER (FK) | → actions.id |
| damage_type | TEXT | |
| damage_dice | TEXT | |

### sub_actions
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| action_id | INTEGER (FK) | → actions.id |
| action_name | TEXT | |
| count | TEXT | |
| type | TEXT | |

### legendary_actions
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| monster_id | INTEGER (FK) | → monsters.id |
| name | TEXT | |
| desc | TEXT | |
| attack_bonus | INTEGER | |
| dc_type | TEXT | |
| dc_value | INTEGER | |
| success_type | TEXT | |
| usage_type | TEXT | |
| usage_times | INTEGER | |

### legendary_action_damage
| Column | Type | Notes |
|---|---|---|
| id | INTEGER (PK) | |
| legendary_action_id | INTEGER (FK) | → legendary_actions.id |
| damage_type | TEXT | |
| damage_dice | TEXT | |