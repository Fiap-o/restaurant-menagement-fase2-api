package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.domain.model.Endereco;

public interface EnderecoMapper {

    Endereco toEntity(EnderecoDTO dto);

    EnderecoDTO toDTO(Endereco endereco);
}
