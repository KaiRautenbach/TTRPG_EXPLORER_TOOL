package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecialAbility {
    private String name;
    private String desc;
    private List<Damage> damage;
    private Dc dc;         // optional — only some abilities have it
    private Usage usage;

    //--------//
    //GETTERS//
    //------//
    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
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

    //--------//
    //SETTERS//
    //------//
    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
}
