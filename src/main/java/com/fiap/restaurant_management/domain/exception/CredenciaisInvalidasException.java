package com.fiap.restaurant_management.domain.exception;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("Login ou senha inválidos");
    }
}
