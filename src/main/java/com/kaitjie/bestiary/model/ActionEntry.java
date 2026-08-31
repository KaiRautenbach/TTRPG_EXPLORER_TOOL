package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ActionEntry {
    private String name;
    private String desc;
    @JsonProperty("attack_bonus")
    private Integer attackBonus;
    private List<Damage> damage;
    private Dc dc;
    private Usage usage;
    @JsonProperty("multiattack_type")
    private String multiattackType;
    private List<SubAction> actions;

    //--------//
    //GETTERS//
    //------//
    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getAttackBonus() {
        return attackBonus;
    }

    public List<Damage> getDamage() {
        return damage;
    }

    public Dc getDc() {
        return dc;
    }

    public Usage getUsage() {
        return usage;
    }

    public String getMultiattackType() {
        return multiattackType;
    }

    public List<SubAction> getActions() {
        return actions;
    }

    //--------//
    //SETTERS//
    //------//
    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setAttackBonus(Integer attackBonus) {
        this.attackBonus = attackBonus;
    }

    public void setDamage(List<Damage> damage) {
        this.damage = damage;
    }

    public void setDc(Dc dc) {
        this.dc = dc;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public void setMultiattackType(String multiattackType) {
        this.multiattackType = multiattackType;
    }

    public void setActions(List<SubAction> actions) {
        this.actions = actions;
    }
}
