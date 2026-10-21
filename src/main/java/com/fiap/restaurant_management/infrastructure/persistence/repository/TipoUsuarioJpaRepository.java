package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.infrastructure.persistence.entity.TipoUsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoUsuarioJpaRepository extends JpaRepository<TipoUsuarioJpaEntity, Long> {

    boolean existsByNome(String nome);
}
