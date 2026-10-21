package com.fiap.restaurant_management.domain.repository;

import com.fiap.restaurant_management.domain.model.TipoUsuario;

import java.util.List;
import java.util.Optional;

public interface TipoUsuarioRepository {

    TipoUsuario save(TipoUsuario tipoUsuario);

    void delete(TipoUsuario tipoUsuario);

    Optional<TipoUsuario> findById(Long id);

    List<TipoUsuario> findAll();

    boolean existsByNome(String nome);
}
