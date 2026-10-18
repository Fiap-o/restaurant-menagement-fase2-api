package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.infrastructure.persistence.entity.UsuarioJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioJpaEntity, Long> {

    Optional<UsuarioJpaEntity> findByLogin(String login);

    List<UsuarioJpaEntity> findByNomeContainingIgnoreCase(String nome);

    boolean existsByEmail(String email);
}
