package com.kaitjie.bestiary.model;

public class Usage {
    private String type;
    private Integer times;

    //--------//
    //GETTERS//
    //------//
    public String getType() {
        return type;
    }

    public Integer getTimes() {
        return times;
    }

    //--------//
    //SETTERS//
    //------//
    public void setType(String type) {
        this.type = type;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}
