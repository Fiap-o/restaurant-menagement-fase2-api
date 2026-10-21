package com.fiap.restaurant_management.application.usecase.itemcardapio;

import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.application.mapper.ItemCardapioMapper;
import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.domain.repository.ItemCardapioRepository;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarItemCardapioPorRestauranteUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final RestauranteRepository restauranteRepository;
    private final ItemCardapioMapper itemCardapioMapper;

    public List<ItemCardapioResponseDTO> executar(Long restauranteId) {
        if (restauranteRepository.findById(restauranteId).isEmpty()) {
            throw new RestauranteNaoEncontradoException(restauranteId);
        }

        return itemCardapioRepository.findByRestauranteId(restauranteId)
                .stream()
                .map(itemCardapioMapper::toResponseDTO)
                .toList();
    }
}
