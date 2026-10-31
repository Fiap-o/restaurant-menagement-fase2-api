package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.infrastructure.persistence.entity.TipoUsuarioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.EnderecoEntityMapper;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.TipoUsuarioEntityMapper;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.UsuarioEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({UsuarioRepositoryImpl.class, UsuarioEntityMapper.class, TipoUsuarioEntityMapper.class, EnderecoEntityMapper.class})
class UsuarioRepositoryImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepositoryImpl repository;

    private TipoUsuarioJpaEntity tipoAdministrador;

    @BeforeEach
    void setUp() {
        tipoAdministrador = entityManager.persistFlushFind(
                TipoUsuarioJpaEntity.builder().nome("Administrador").build());
    }

    private UsuarioJpaEntity novoUsuarioEntity(String nome, String email, String login) {
        return UsuarioJpaEntity.builder()
                .nome(nome)
                .email(email)
                .login(login)
                .senha("senha-criptografada")
                .tipoUsuario(tipoAdministrador)
                .dataUltimaAlteracao(LocalDateTime.now())
                .build();
    }

    @Test
    void save_devePersistirEGerarId() {
        Usuario usuario = Usuario.builder()
                .nome("Sophia Amaral Silva")
                .email("sophia.amaral@fiap.com.br")
                .login("sophia.amaral")
                .senha("senha-criptografada")
                .tipoUsuario(TipoUsuario.builder().id(tipoAdministrador.getId()).nome(tipoAdministrador.getNome()).build())
                .dataUltimaAlteracao(LocalDateTime.now())
                .build();

        Usuario salvo = repository.save(usuario);

        assertThat(salvo.getId()).isNotNull();
        assertThat(entityManager.find(UsuarioJpaEntity.class, salvo.getId()).getEmail())
                .isEqualTo("sophia.amaral@fiap.com.br");
    }

    @Test
    void findById_deveRetornarUsuario_quandoExiste() {
        UsuarioJpaEntity entity = entityManager.persistFlushFind(
                novoUsuarioEntity("Erick Roberto Ribeiro", "erick.ribeiro@fiap.com.br", "erick.ribeiro"));

        Optional<Usuario> resultado = repository.findById(entity.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Erick Roberto Ribeiro");
    }

    @Test
    void findById_deveRetornarVazio_quandoNaoExiste() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    void findByLogin_deveRetornarUsuario_quandoLoginExiste() {
        entityManager.persistAndFlush(
                novoUsuarioEntity("Sophia Amaral Silva", "sophia.amaral@fiap.com.br", "sophia.amaral"));

        Optional<Usuario> resultado = repository.findByLogin("sophia.amaral");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("sophia.amaral@fiap.com.br");
    }

    @Test
    void findByLogin_deveRetornarVazio_quandoLoginNaoExiste() {
        assertThat(repository.findByLogin("inexistente")).isEmpty();
    }

    @Test
    void findByNomeContainingIgnoreCase_deveRetornarUsuariosComNomeSemelhante() {
        entityManager.persistAndFlush(
                novoUsuarioEntity("Sophia Amaral Silva", "sophia.amaral@fiap.com.br", "sophia.amaral"));
        entityManager.persistAndFlush(
                novoUsuarioEntity("Erick Roberto Ribeiro", "erick.ribeiro@fiap.com.br", "erick.ribeiro"));

        List<Usuario> resultado = repository.findByNomeContainingIgnoreCase("amaral");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Sophia Amaral Silva");
    }

    @Test
    void existsByEmail_deveRetornarTrue_quandoEmailJaCadastrado() {
        entityManager.persistAndFlush(
                novoUsuarioEntity("Erick Roberto Ribeiro", "erick.ribeiro@fiap.com.br", "erick.ribeiro"));

        assertThat(repository.existsByEmail("erick.ribeiro@fiap.com.br")).isTrue();
        assertThat(repository.existsByEmail("inexistente@fiap.com.br")).isFalse();
    }

    @Test
    void delete_deveRemoverUsuario() {
        UsuarioJpaEntity entity = entityManager.persistFlushFind(
                novoUsuarioEntity("Sophia Amaral Silva", "sophia.amaral@fiap.com.br", "sophia.amaral"));
        Usuario usuario = Usuario.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .login(entity.getLogin())
                .senha(entity.getSenha())
                .tipoUsuario(TipoUsuario.builder().id(tipoAdministrador.getId()).nome(tipoAdministrador.getNome()).build())
                .dataUltimaAlteracao(entity.getDataUltimaAlteracao())
                .build();

        repository.delete(usuario);

        assertThat(entityManager.find(UsuarioJpaEntity.class, entity.getId())).isNull();
    }
}
