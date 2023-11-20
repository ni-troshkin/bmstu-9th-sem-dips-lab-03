package com.libraryservice.entity;

public enum Condition {
    EXCELLENT ("EXCELLENT"),
    GOOD ("GOOD"),
    BAD ("BAD");

    private String title;

    Condition(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
