package com.fiap.restaurant_management.application.usecase.itemcardapio;

import com.fiap.restaurant_management.application.dto.ItemCardapioCreateDTO;
import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.application.mapper.ItemCardapioMapper;
import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.repository.ItemCardapioRepository;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarItemCardapioUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final RestauranteRepository restauranteRepository;
    private final ItemCardapioMapper itemCardapioMapper;

    public ItemCardapioResponseDTO executar(ItemCardapioCreateDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
                .orElseThrow(() -> new RestauranteNaoEncontradoException(dto.getRestauranteId()));

        ItemCardapio itemCardapio = itemCardapioMapper.toEntity(dto);
        itemCardapio.setRestaurante(restaurante);

        ItemCardapio salvo = itemCardapioRepository.save(itemCardapio);
        return itemCardapioMapper.toResponseDTO(salvo);
    }
}
