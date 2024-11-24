package com.brigada.carsh.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String msg) {
        super(msg);
    }
}
