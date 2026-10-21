package com.fiap.restaurant_management.application.usecase.itemcardapio;

import com.fiap.restaurant_management.domain.exception.ItemCardapioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.repository.ItemCardapioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExcluirItemCardapioUseCase {

    private final ItemCardapioRepository itemCardapioRepository;

    public void executar(Long id) {
        ItemCardapio itemCardapio = itemCardapioRepository.findById(id)
                .orElseThrow(() -> new ItemCardapioNaoEncontradoException(id));
        itemCardapioRepository.delete(itemCardapio);
    }
}
