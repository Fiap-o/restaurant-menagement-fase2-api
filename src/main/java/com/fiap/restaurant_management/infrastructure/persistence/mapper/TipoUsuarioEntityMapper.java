package com.fiap.restaurant_management.infrastructure.persistence.mapper;

import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.infrastructure.persistence.entity.TipoUsuarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TipoUsuarioEntityMapper {

    public TipoUsuarioJpaEntity toJpaEntity(TipoUsuario tipoUsuario) {
        if (tipoUsuario == null) {
            return null;
        }
        return TipoUsuarioJpaEntity.builder()
                .id(tipoUsuario.getId())
                .nome(tipoUsuario.getNome())
                .build();
    }

    public TipoUsuario toDomain(TipoUsuarioJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return TipoUsuario.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .build();
    }
}
