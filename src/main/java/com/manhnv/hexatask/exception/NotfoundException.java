package com.manhnv.hexatask.exception;

import lombok.Getter;

@Getter
public class NotfoundException extends RuntimeException{
    private String message;

    public NotfoundException(String message) {
        this.message = message;
    }
}
