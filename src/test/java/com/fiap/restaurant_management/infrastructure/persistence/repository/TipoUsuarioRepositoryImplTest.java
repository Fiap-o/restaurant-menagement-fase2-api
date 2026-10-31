package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.infrastructure.persistence.entity.TipoUsuarioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.TipoUsuarioEntityMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TipoUsuarioRepositoryImpl.class, TipoUsuarioEntityMapper.class})
class TipoUsuarioRepositoryImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TipoUsuarioRepositoryImpl repository;

    @Test
    void save_devePersistirEGerarId() {
        TipoUsuario tipoUsuario = TipoUsuario.builder().nome("Administrador").build();

        TipoUsuario salvo = repository.save(tipoUsuario);

        assertThat(salvo.getId()).isNotNull();
        assertThat(entityManager.find(TipoUsuarioJpaEntity.class, salvo.getId()).getNome())
                .isEqualTo("Administrador");
    }

    @Test
    void findById_deveRetornarTipoUsuario_quandoExiste() {
        TipoUsuarioJpaEntity entity = entityManager.persistFlushFind(
                TipoUsuarioJpaEntity.builder().nome("Dono de Restaurante").build());

        Optional<TipoUsuario> resultado = repository.findById(entity.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Dono de Restaurante");
    }

    @Test
    void findById_deveRetornarVazio_quandoNaoExiste() {
        Optional<TipoUsuario> resultado = repository.findById(999L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void findAll_deveRetornarTodosOsTiposCadastrados() {
        entityManager.persistAndFlush(TipoUsuarioJpaEntity.builder().nome("Administrador").build());
        entityManager.persistAndFlush(TipoUsuarioJpaEntity.builder().nome("Cliente").build());

        List<TipoUsuario> resultado = repository.findAll();

        assertThat(resultado).hasSize(2);
    }

    @Test
    void existsByNome_deveRetornarTrue_quandoNomeJaCadastrado() {
        entityManager.persistAndFlush(TipoUsuarioJpaEntity.builder().nome("Administrador").build());

        assertThat(repository.existsByNome("Administrador")).isTrue();
        assertThat(repository.existsByNome("Inexistente")).isFalse();
    }

    @Test
    void delete_deveRemoverTipoUsuario() {
        TipoUsuarioJpaEntity entity = entityManager.persistFlushFind(
                TipoUsuarioJpaEntity.builder().nome("Cliente").build());
        TipoUsuario tipoUsuario = TipoUsuario.builder().id(entity.getId()).nome(entity.getNome()).build();

        repository.delete(tipoUsuario);

        assertThat(entityManager.find(TipoUsuarioJpaEntity.class, entity.getId())).isNull();
    }
}
