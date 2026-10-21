package com.fiap.restaurant_management.application.usecase.restaurante;

import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.application.mapper.RestauranteMapper;
import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarRestaurantePorIdUseCase {

    private final RestauranteRepository restauranteRepository;
    private final RestauranteMapper restauranteMapper;

    public RestauranteResponseDTO executar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(id));
        return restauranteMapper.toResponseDTO(restaurante);
    }
}
