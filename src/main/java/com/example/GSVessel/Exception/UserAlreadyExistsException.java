package com.example.GSVessel.Exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String field, String value) {
        super("Usuario con " + field + " '" + value + "' ya existe");
    }
}
