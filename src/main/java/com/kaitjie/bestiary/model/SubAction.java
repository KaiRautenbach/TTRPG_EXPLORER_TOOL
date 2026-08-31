package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubAction {
    @JsonProperty("action_name")
    private String actionName;
    private String count;
    private String type;

    //--------//
    //GETTERS//
    //------//
    public String getActionName() {
        return actionName;
    }

    public String getCount() {
        return count;
    }

    public String getType() {
        return type;
    }

    //--------//
    //SETTERS//
    //------//
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setType(String type) {
        this.type = type;
    }
}
