package com.example.moduleexternalapi.api;

public enum BlogSearchSortType {
    ACCURACY("accuracy"),
    RECENCY("recency"),
    SIM("sim"),
    DATE("date");

    private final String value;

    BlogSearchSortType(String value) {
        this.value = value;
    }

}