package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.repository.ItemCardapioRepository;
import com.fiap.restaurant_management.infrastructure.persistence.entity.ItemCardapioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.ItemCardapioEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemCardapioRepositoryImpl implements ItemCardapioRepository {

    private final ItemCardapioJpaRepository jpaRepository;
    private final ItemCardapioEntityMapper mapper;

    @Override
    public ItemCardapio save(ItemCardapio itemCardapio) {
        ItemCardapioJpaEntity entity = jpaRepository.save(mapper.toJpaEntity(itemCardapio));
        return mapper.toDomain(entity);
    }

    @Override
    public void delete(ItemCardapio itemCardapio) {
        jpaRepository.delete(mapper.toJpaEntity(itemCardapio));
    }

    @Override
    public Optional<ItemCardapio> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ItemCardapio> findByRestauranteId(Long restauranteId) {
        return jpaRepository.findByRestauranteId(restauranteId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
