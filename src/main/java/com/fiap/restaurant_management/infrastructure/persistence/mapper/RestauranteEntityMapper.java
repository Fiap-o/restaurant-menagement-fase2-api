package com.fiap.restaurant_management.infrastructure.persistence.mapper;

import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.infrastructure.persistence.entity.RestauranteJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestauranteEntityMapper {

    private final EnderecoEntityMapper enderecoEntityMapper;
    private final UsuarioEntityMapper usuarioEntityMapper;

    public RestauranteJpaEntity toJpaEntity(Restaurante restaurante) {
        if (restaurante == null) {
            return null;
        }
        return RestauranteJpaEntity.builder()
                .id(restaurante.getId())
                .nome(restaurante.getNome())
                .endereco(enderecoEntityMapper.toEmbeddable(restaurante.getEndereco()))
                .tipoCozinha(restaurante.getTipoCozinha())
                .horarioFuncionamento(restaurante.getHorarioFuncionamento())
                .dono(usuarioEntityMapper.toJpaEntity(restaurante.getDono()))
                .build();
    }

    public Restaurante toDomain(RestauranteJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Restaurante.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .endereco(enderecoEntityMapper.toDomain(entity.getEndereco()))
                .tipoCozinha(entity.getTipoCozinha())
                .horarioFuncionamento(entity.getHorarioFuncionamento())
                .dono(usuarioEntityMapper.toDomain(entity.getDono()))
                .build();
    }
}
