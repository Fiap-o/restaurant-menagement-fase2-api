package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.RestauranteCreateDTO;
import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.domain.model.Restaurante;

public interface RestauranteMapper {

    Restaurante toEntity(RestauranteCreateDTO dto);

    RestauranteResponseDTO toResponseDTO(Restaurante restaurante);
}
