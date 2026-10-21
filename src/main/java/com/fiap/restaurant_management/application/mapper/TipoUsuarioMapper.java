package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.TipoUsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.domain.model.TipoUsuario;

public interface TipoUsuarioMapper {

    TipoUsuario toEntity(TipoUsuarioCreateDTO dto);

    TipoUsuarioResponseDTO toResponseDTO(TipoUsuario tipoUsuario);
}
