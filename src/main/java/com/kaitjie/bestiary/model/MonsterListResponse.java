package com.kaitjie.bestiary.model;

import java.util.List;

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
