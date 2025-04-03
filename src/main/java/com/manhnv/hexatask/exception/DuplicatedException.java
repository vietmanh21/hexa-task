package com.manhnv.hexatask.exception;

import lombok.Getter;

@Getter
public class DuplicatedException extends RuntimeException {
    private String message;

    public DuplicatedException(String message) {
        this.message = message;
    }
}
