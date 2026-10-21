package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.infrastructure.persistence.entity.ItemCardapioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCardapioJpaRepository extends JpaRepository<ItemCardapioJpaEntity, Long> {

    List<ItemCardapioJpaEntity> findByRestauranteId(Long restauranteId);
}
