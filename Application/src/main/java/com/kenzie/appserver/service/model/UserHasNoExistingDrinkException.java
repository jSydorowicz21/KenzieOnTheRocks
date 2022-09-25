package com.kenzie.appserver.service.model;

public class UserHasNoExistingDrinkException extends RuntimeException{
    public UserHasNoExistingDrinkException(String message){
        super(message);

    }
}
