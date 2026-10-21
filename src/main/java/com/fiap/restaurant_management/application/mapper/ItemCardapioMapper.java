package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.ItemCardapioCreateDTO;
import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.domain.model.ItemCardapio;

public interface ItemCardapioMapper {

    ItemCardapio toEntity(ItemCardapioCreateDTO dto);

    ItemCardapioResponseDTO toResponseDTO(ItemCardapio itemCardapio);
}
