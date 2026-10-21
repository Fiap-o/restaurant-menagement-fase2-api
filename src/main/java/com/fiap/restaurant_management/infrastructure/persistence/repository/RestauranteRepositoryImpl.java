package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import com.fiap.restaurant_management.infrastructure.persistence.entity.RestauranteJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.RestauranteEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RestauranteRepositoryImpl implements RestauranteRepository {

    private final RestauranteJpaRepository jpaRepository;
    private final RestauranteEntityMapper mapper;

    @Override
    public Restaurante save(Restaurante restaurante) {
        RestauranteJpaEntity entity = jpaRepository.save(mapper.toJpaEntity(restaurante));
        return mapper.toDomain(entity);
    }

    @Override
    public void delete(Restaurante restaurante) {
        jpaRepository.delete(mapper.toJpaEntity(restaurante));
    }

    @Override
    public Optional<Restaurante> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Restaurante> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
