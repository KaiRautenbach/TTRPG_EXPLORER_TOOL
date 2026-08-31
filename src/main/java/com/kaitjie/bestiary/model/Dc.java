package com.kaitjie.bestiary.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Dc {
    @JsonProperty("dc_type")
    private ApiRef dcType;
    @JsonProperty("dc_value")
    private int dcValue;
    @JsonProperty("success_type")
    private String successType;

    //--------//
    //GETTERS//
    //------//
    public ApiRef getDcType() {
        return dcType;
    }

    public int getDcValue() {
        return dcValue;
    }

    public String getSuccessType() {
        return successType;
    }

    //--------//
    //SETTERS//
    //------//
    public void setDcType(ApiRef dcType) {
        this.dcType = dcType;
    }

    public void setDcValue(int dcValue) {
        this.dcValue = dcValue;
    }

    public void setSuccessType(String successType) {
        this.successType = successType;
    }
}
