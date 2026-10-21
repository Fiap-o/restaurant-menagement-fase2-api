package com.fiap.restaurant_management.domain.exception;

public class TipoUsuarioJaCadastradoException extends RuntimeException {
    public TipoUsuarioJaCadastradoException(String nome) {
        super("Tipo de usuário já cadastrado: " + nome);
    }
}
