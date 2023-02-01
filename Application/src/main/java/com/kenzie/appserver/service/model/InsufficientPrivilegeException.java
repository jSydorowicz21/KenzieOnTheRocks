package com.kenzie.appserver.service.model;

public class InsufficientPrivilegeException extends RuntimeException{
    public InsufficientPrivilegeException(String message){
        super(message);
    }
}
