package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.UsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.domain.model.Usuario;

public interface UsuarioMapper {

    Usuario toEntity(UsuarioCreateDTO dto);

    UsuarioResponseDTO toResponseDTO(Usuario usuario);
}
