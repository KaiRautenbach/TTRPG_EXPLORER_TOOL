package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonsterListResponse {

    private int count;
    private List<MonsterSummary> results;

    //--------//
    //GETTERS//
    //------//
    public int getCount() {
        return count;
    }

    public List<MonsterSummary> getResults() {
        return results;
    }

    //--------//
    //SETTERS//
    //------//
    public void setCount(int count) {
        this.count = count;
    }

    public void setResults(List<MonsterSummary> results) {
        this.results = results;
    }
}
