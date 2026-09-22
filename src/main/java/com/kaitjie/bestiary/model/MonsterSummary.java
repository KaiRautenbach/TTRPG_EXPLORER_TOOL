package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MonsterSummary {

    private String index;
    private String name;
    private String url;

    //--------//
    //GETTERS//
    //------//
    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
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

    public void setUrl(String url) {
        this.url = url;
    }
}
