package com.fiap.restaurant_management.infrastructure.persistence.mapper;

import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.infrastructure.persistence.entity.ItemCardapioJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemCardapioEntityMapper {

    private final RestauranteEntityMapper restauranteEntityMapper;

    public ItemCardapioJpaEntity toJpaEntity(ItemCardapio itemCardapio) {
        if (itemCardapio == null) {
            return null;
        }
        return ItemCardapioJpaEntity.builder()
                .id(itemCardapio.getId())
                .nome(itemCardapio.getNome())
                .descricao(itemCardapio.getDescricao())
                .preco(itemCardapio.getPreco())
                .disponivelSomenteNoLocal(itemCardapio.isDisponivelSomenteNoLocal())
                .caminhoFoto(itemCardapio.getCaminhoFoto())
                .restaurante(restauranteEntityMapper.toJpaEntity(itemCardapio.getRestaurante()))
                .build();
    }

    public ItemCardapio toDomain(ItemCardapioJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return ItemCardapio.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .preco(entity.getPreco())
                .disponivelSomenteNoLocal(entity.isDisponivelSomenteNoLocal())
                .caminhoFoto(entity.getCaminhoFoto())
                .restaurante(restauranteEntityMapper.toDomain(entity.getRestaurante()))
                .build();
    }
}
