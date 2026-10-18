package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import com.fiap.restaurant_management.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.UsuarioEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;
    private final UsuarioEntityMapper mapper;

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioJpaEntity entity = jpaRepository.save(mapper.toJpaEntity(usuario));
        return mapper.toDomain(entity);
    }

    @Override
    public void delete(Usuario usuario) {
        jpaRepository.delete(mapper.toJpaEntity(usuario));
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Usuario> findByLogin(String login) {
        return jpaRepository.findByLogin(login).map(mapper::toDomain);
    }

    @Override
    public List<Usuario> findByNomeContainingIgnoreCase(String nome) {
        return jpaRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
