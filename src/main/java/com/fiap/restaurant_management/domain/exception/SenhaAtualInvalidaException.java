package com.fiap.restaurant_management.domain.exception;

public class SenhaAtualInvalidaException extends RuntimeException {
    public SenhaAtualInvalidaException() {
        super("Senha atual informada está incorreta");
    }
}
