package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Monster {

    private String index;
    private String name;
    private String size;
    private String type;
    private String alignment;

    @JsonProperty("armor_class")
    private List<ArmorClass> armorClass;

    @JsonProperty("hit_points")
    private int hitPoints;

    @JsonProperty("hit_dice")
    private String hitDice;

    @JsonProperty("hit_points_roll")
    private String hitPointsRoll;

    private Speed speed;

    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;

    private List<Proficiency> proficiencies;

    @JsonProperty("damage_vulnerabilities")
    private List<String> damageVulnerabilities;

    @JsonProperty("damage_resistances")
    private List<String> damageResistances;

    @JsonProperty("damage_immunities")
    private List<String> damageImmunities;

    @JsonProperty("condition_immunities")
    private List<ApiRef> conditionImmunities;

    private Senses senses;

    private String languages;

    @JsonProperty("challenge_rating")
    private double challengeRating;

    @JsonProperty("proficiency_bonus")
    private int proficiencyBonus;

    private int xp;

    @JsonProperty("special_abilities")
    private List<SpecialAbility> specialAbilities;

    private List<ActionEntry> actions;

    @JsonProperty("legendary_actions")
    private List<ActionEntry> legendaryActions;

    private String image;

    private String url;

    @JsonProperty("updated_at")
    private String lastUpdated;

    //--------//
    //GETTERS//
    //------//
    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getAlignment() {
        return alignment;
    }

    public List<ArmorClass> getArmorClass() {
        return armorClass;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public String getHitDice() {
        return hitDice;
    }

    public String getHitPointsRoll() {
        return hitPointsRoll;
    }

    public Speed getSpeed() {
        return speed;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public List<Proficiency> getProficiencies() {
        return proficiencies;
    }

    public List<String> getDamageVulnerabilities() {
        return damageVulnerabilities;
    }

    public List<String> getDamageResistances() {
        return damageResistances;
    }

    public List<String> getDamageImmunities() {
        return damageImmunities;
    }

    public List<ApiRef> getConditionImmunities() {
        return conditionImmunities;
    }

    public Senses getSenses() {
        return senses;
    }

    public String getLanguages() {
        return languages;
    }

    public double getChallengeRating() {
        return challengeRating;
    }

    public int getProficiencyBonus() {
        return proficiencyBonus;
    }

    public int getXp() {
        return xp;
    }

    public List<SpecialAbility> getSpecialAbilities() {
        return specialAbilities;
    }

    public List<ActionEntry> getActions() {
        return actions;
    }

    public List<ActionEntry> getLegendaryActions() {
        return legendaryActions;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    //--------//
    //SETTERS//
    //------//
    public void setIndex(String index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public void setArmorClass(List<ArmorClass> armorClass) {
        this.armorClass = armorClass;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setHitDice(String hitDice) {
        this.hitDice = hitDice;
    }

    public void setHitPointsRoll(String hitPointsRoll) {
        this.hitPointsRoll = hitPointsRoll;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public void setProficiencies(List<Proficiency> proficiencies) {
        this.proficiencies = proficiencies;
    }

    public void setDamageVulnerabilities(List<String> damageVulnerabilities) {
        this.damageVulnerabilities = damageVulnerabilities;
    }

    public void setDamageResistances(List<String> damageResistances) {
        this.damageResistances = damageResistances;
    }

    public void setDamageImmunities(List<String> damageImmunities) {
        this.damageImmunities = damageImmunities;
    }

    public void setConditionImmunities(List<ApiRef> conditionImmunities) {
        this.conditionImmunities = conditionImmunities;
    }

    public void setSenses(Senses senses) {
        this.senses = senses;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public void setChallengeRating(double challengeRating) {
        this.challengeRating = challengeRating;
    }

    public void setProficiencyBonus(int proficiencyBonus) {
        this.proficiencyBonus = proficiencyBonus;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setSpecialAbilities(List<SpecialAbility> specialAbilities) {
        this.specialAbilities = specialAbilities;
    }

    public void setActions(List<ActionEntry> actions) {
        this.actions = actions;
    }

    public void setLegendaryActions(List<ActionEntry> legendaryActions) {
        this.legendaryActions = legendaryActions;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
