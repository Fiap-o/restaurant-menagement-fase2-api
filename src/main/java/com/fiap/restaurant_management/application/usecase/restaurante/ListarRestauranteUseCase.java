package com.fiap.restaurant_management.application.usecase.restaurante;

import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.application.mapper.RestauranteMapper;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final RestauranteMapper restauranteMapper;

    public List<RestauranteResponseDTO> executar() {
        return restauranteRepository.findAll()
                .stream()
                .map(restauranteMapper::toResponseDTO)
                .toList();
    }
}
