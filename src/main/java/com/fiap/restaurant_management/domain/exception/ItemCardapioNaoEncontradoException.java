package com.fiap.restaurant_management.domain.exception;

public class ItemCardapioNaoEncontradoException extends RuntimeException {
    public ItemCardapioNaoEncontradoException(Long id) {
        super("Item de cardápio não encontrado com id: " + id);
    }
}
