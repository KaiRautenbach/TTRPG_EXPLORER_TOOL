package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Senses {
    @JsonProperty("darkvision")
    private String darkVision;

    @JsonProperty("blindsight")
    private String blindSight;

    @JsonProperty("tremorsense")
    private String tremorSense;

    @JsonProperty("truesight")
    private String trueSight;

    @JsonProperty("passive_perception")
    private int passivePerception;

    //--------//
    //GETTERS//
    //------//
    public String getDarkVision() {
        return darkVision;
    }

    public String getBlindSight() {
        return blindSight;
    }

    public String getTremorSense() {
        return tremorSense;
    }

    public String getTrueSight() {
        return trueSight;
    }

    public int getPassivePerception() {
        return passivePerception;
    }

    //--------//
    //SETTERS//
    //------//
    public void setDarkVision(String darkVision) {
        this.darkVision = darkVision;
    }

    public void setBlindSight(String blindSight) {
        this.blindSight = blindSight;
    }

    public void setTremorSense(String tremorSense) {
        this.tremorSense = tremorSense;
    }

    public void setTrueSight(String trueSight) {
        this.trueSight = trueSight;
    }

    public void setPassivePerception(int passivePerception) {
        this.passivePerception = passivePerception;
    }
}
