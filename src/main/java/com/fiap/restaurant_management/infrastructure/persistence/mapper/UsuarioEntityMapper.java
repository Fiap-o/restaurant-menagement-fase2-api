package com.fiap.restaurant_management.infrastructure.persistence.mapper;

import com.fiap.restaurant_management.domain.model.Endereco;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.infrastructure.persistence.entity.EnderecoEmbeddable;
import com.fiap.restaurant_management.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioEntityMapper {

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
                .tipoUsuario(usuario.getTipoUsuario())
                .endereco(toEmbeddable(usuario.getEndereco()))
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
                .tipoUsuario(entity.getTipoUsuario())
                .endereco(toDomain(entity.getEndereco()))
                .dataUltimaAlteracao(entity.getDataUltimaAlteracao())
                .build();
    }

    private EnderecoEmbeddable toEmbeddable(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoEmbeddable(
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getCep()
        );
    }

    private Endereco toDomain(EnderecoEmbeddable embeddable) {
        if (embeddable == null) {
            return null;
        }
        return new Endereco(
                embeddable.getRua(),
                embeddable.getNumero(),
                embeddable.getCidade(),
                embeddable.getCep()
        );
    }
}
