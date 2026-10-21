package com.fiap.restaurant_management.application.usecase.itemcardapio;

import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.application.mapper.ItemCardapioMapper;
import com.fiap.restaurant_management.domain.exception.ItemCardapioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.repository.ItemCardapioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarItemCardapioPorIdUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final ItemCardapioMapper itemCardapioMapper;

    public ItemCardapioResponseDTO executar(Long id) {
        ItemCardapio itemCardapio = itemCardapioRepository.findById(id)
                .orElseThrow(() -> new ItemCardapioNaoEncontradoException(id));
        return itemCardapioMapper.toResponseDTO(itemCardapio);
    }
}
