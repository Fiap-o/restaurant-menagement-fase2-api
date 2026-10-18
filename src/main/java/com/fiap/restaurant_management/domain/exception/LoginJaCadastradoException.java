package com.fiap.restaurant_management.domain.exception;

public class LoginJaCadastradoException extends RuntimeException {
    public LoginJaCadastradoException(String login) {
        super("Login já cadastrado: " + login);
    }
}
