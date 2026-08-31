package com.kaitjie.bestiary.model;

public class Proficiency {
    private int value;
    private ApiRef proficiency;

    //--------//
    //GETTERS//
    //------//
    public int getValue() {
        return value;
    }

    public ApiRef getProficiency() {
        return proficiency;
    }

    //--------//
    //SETTERS//
    //------//
    public void setValue(int value) {
        this.value = value;
    }

    public void setProficiency(ApiRef proficiency) {
        this.proficiency = proficiency;
    }
}
