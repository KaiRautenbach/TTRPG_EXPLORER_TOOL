package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Damage {
    @JsonProperty("damage_type")
    private ApiRef damageType;
    @JsonProperty("damage_dice")
    private String damageDice;

    //--------//
    //GETTERS//
    //------//
    public ApiRef getDamageType() {
        return damageType;
    }

    public String getDamageDice() {
        return damageDice;
    }

    //--------//
    //SETTERS//
    //------//
    public void setDamageType(ApiRef damageType) {
        this.damageType = damageType;
    }

    public void setDamageDice(String damageDice) {
        this.damageDice = damageDice;
    }
}
