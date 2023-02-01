package com.kenzie.appserver.service.model;

public class UserHasExistingDrinkException extends RuntimeException{
    public UserHasExistingDrinkException( String message) {
        super(message);
    }
}
