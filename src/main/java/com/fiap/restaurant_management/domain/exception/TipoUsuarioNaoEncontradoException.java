package com.fiap.restaurant_management.domain.exception;

public class TipoUsuarioNaoEncontradoException extends RuntimeException {
    public TipoUsuarioNaoEncontradoException(Long id) {
        super("Tipo de usuário não encontrado com id: " + id);
    }
}
