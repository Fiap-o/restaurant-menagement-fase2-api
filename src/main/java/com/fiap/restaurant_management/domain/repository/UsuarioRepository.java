package com.fiap.restaurant_management.domain.repository;

import com.fiap.restaurant_management.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {

    Usuario save(Usuario usuario);

    void delete(Usuario usuario);

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByLogin(String login);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    boolean existsByEmail(String email);
}
