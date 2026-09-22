package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArmorClass {
    private String type;
    private int value;

    //--------//
    //GETTERS//
    //------//
    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    //--------//
    //SETTERS//
    //------//
    public void setType(String type) {
        this.type = type;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
