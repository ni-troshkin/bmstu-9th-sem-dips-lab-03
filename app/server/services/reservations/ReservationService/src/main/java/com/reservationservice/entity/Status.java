package com.reservationservice.entity;

public enum Status {
    RENTED ("RENTED"),
    RETURNED ("RETURNED"),
    EXPIRED ("EXPIRED");
    private String title;

    Status(String title) {
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
