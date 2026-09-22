package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Speed {
    private String walk;
    private String fly;
    private String swim;
    private String climb;
    private String burrow;
    private Boolean hover;

    //--------//
    //GETTERS//
    //------//
    public String getWalk() {
        return walk;
    }

    public String getFly() {
        return fly;
    }

    public String getSwim() {
        return swim;
    }

    public String getClimb() {
        return climb;
    }

    public String getBurrow() {
        return burrow;
    }

    public Boolean getHover() {
        return hover;
    }

    //--------//
    //SETTERS//
    //------//
    public void setHover(Boolean hover) {
        this.hover = hover;
    }

    public void setBurrow(String burrow) {
        this.burrow = burrow;
    }

    public void setClimb(String climb) {
        this.climb = climb;
    }

    public void setSwim(String swim) {
        this.swim = swim;
    }

    public void setFly(String fly) {
        this.fly = fly;
    }

    public void setWalk(String walk) {
        this.walk = walk;
    }
}
