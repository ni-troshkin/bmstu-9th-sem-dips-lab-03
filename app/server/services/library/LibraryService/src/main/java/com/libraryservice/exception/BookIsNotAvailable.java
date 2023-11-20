package com.libraryservice.exception;

public class BookIsNotAvailable extends Exception {
    public BookIsNotAvailable(String message) {
        super(message);
    }
}