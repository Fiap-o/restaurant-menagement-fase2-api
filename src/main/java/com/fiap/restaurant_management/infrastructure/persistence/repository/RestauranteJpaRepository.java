package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.infrastructure.persistence.entity.RestauranteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestauranteJpaRepository extends JpaRepository<RestauranteJpaEntity, Long> {
}
