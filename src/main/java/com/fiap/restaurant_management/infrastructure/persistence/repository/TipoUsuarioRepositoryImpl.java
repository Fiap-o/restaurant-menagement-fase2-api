package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import com.fiap.restaurant_management.infrastructure.persistence.entity.TipoUsuarioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.TipoUsuarioEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TipoUsuarioRepositoryImpl implements TipoUsuarioRepository {

    private final TipoUsuarioJpaRepository jpaRepository;
    private final TipoUsuarioEntityMapper mapper;

    @Override
    public TipoUsuario save(TipoUsuario tipoUsuario) {
        TipoUsuarioJpaEntity entity = jpaRepository.save(mapper.toJpaEntity(tipoUsuario));
        return mapper.toDomain(entity);
    }

    @Override
    public void delete(TipoUsuario tipoUsuario) {
        jpaRepository.delete(mapper.toJpaEntity(tipoUsuario));
    }

    @Override
    public Optional<TipoUsuario> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<TipoUsuario> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByNome(String nome) {
        return jpaRepository.existsByNome(nome);
    }
}
