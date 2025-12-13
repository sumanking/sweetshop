package com.incubyte.sweetshop.exception;

public class SweetNotFoundException extends RuntimeException {

    public SweetNotFoundException(Long id) {
        super("Sweet not found with id: " + id);
    }
}
