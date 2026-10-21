package com.fiap.restaurant_management.domain.exception;

public class RestauranteNaoEncontradoException extends RuntimeException {
    public RestauranteNaoEncontradoException(Long id) {
        super("Restaurante não encontrado com id: " + id);
    }
}
