package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.TipoUsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import org.springframework.stereotype.Component;

@Component
public class TipoUsuarioMapperImpl implements TipoUsuarioMapper {

    @Override
    public TipoUsuario toEntity(TipoUsuarioCreateDTO dto) {
        if (dto == null) {
            return null;
        }
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setNome(dto.getNome());
        return tipoUsuario;
    }

    @Override
    public TipoUsuarioResponseDTO toResponseDTO(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }
        return TipoUsuarioResponseDTO.builder()
                .id(tipoUsuario.getId())
                .nome(tipoUsuario.getNome())
                .build();
    }
}
