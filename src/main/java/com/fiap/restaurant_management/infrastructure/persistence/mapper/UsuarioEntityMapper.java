package com.fiap.restaurant_management.infrastructure.persistence.mapper;

import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.infrastructure.persistence.entity.UsuarioJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioEntityMapper {

    private final TipoUsuarioEntityMapper tipoUsuarioEntityMapper;
    private final EnderecoEntityMapper enderecoEntityMapper;

    public UsuarioJpaEntity toJpaEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return UsuarioJpaEntity.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .login(usuario.getLogin())
                .senha(usuario.getSenha())
                .tipoUsuario(tipoUsuarioEntityMapper.toJpaEntity(usuario.getTipoUsuario()))
                .endereco(enderecoEntityMapper.toEmbeddable(usuario.getEndereco()))
                .dataUltimaAlteracao(usuario.getDataUltimaAlteracao())
                .build();
    }

    public Usuario toDomain(UsuarioJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Usuario.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .login(entity.getLogin())
                .senha(entity.getSenha())
                .tipoUsuario(tipoUsuarioEntityMapper.toDomain(entity.getTipoUsuario()))
                .endereco(enderecoEntityMapper.toDomain(entity.getEndereco()))
                .dataUltimaAlteracao(entity.getDataUltimaAlteracao())
                .build();
    }
}
