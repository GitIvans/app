package com.accenture.sms.handlers;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }

}