package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.infrastructure.persistence.entity.RestauranteJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.entity.TipoUsuarioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.EnderecoEntityMapper;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.RestauranteEntityMapper;
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
@Import({
        RestauranteRepositoryImpl.class, RestauranteEntityMapper.class,
        UsuarioEntityMapper.class, TipoUsuarioEntityMapper.class, EnderecoEntityMapper.class
})
class RestauranteRepositoryImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RestauranteRepositoryImpl repository;

    private UsuarioJpaEntity dono;

    @BeforeEach
    void setUp() {
        TipoUsuarioJpaEntity tipoUsuario = entityManager.persistFlushFind(
                TipoUsuarioJpaEntity.builder().nome("Dono de Restaurante").build());

        dono = entityManager.persistFlushFind(UsuarioJpaEntity.builder()
                .nome("Sophia Amaral Silva")
                .email("sophia.amaral@fiap.com.br")
                .login("sophia.amaral")
                .senha("senha-criptografada")
                .tipoUsuario(tipoUsuario)
                .dataUltimaAlteracao(LocalDateTime.now())
                .build());
    }

    private RestauranteJpaEntity novoRestauranteEntity(String nome) {
        return RestauranteJpaEntity.builder()
                .nome(nome)
                .tipoCozinha("Italiana")
                .horarioFuncionamento("11:00-23:00")
                .dono(dono)
                .build();
    }

    @Test
    void save_devePersistirEGerarId() {
        Restaurante restaurante = Restaurante.builder()
                .nome("Cantina da Sophia")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("11:00-23:00")
                .dono(Usuario.builder().id(dono.getId()).nome(dono.getNome()).build())
                .build();

        Restaurante salvo = repository.save(restaurante);

        assertThat(salvo.getId()).isNotNull();
        assertThat(entityManager.find(RestauranteJpaEntity.class, salvo.getId()).getNome())
                .isEqualTo("Cantina da Sophia");
    }

    @Test
    void findById_deveRetornarRestaurante_quandoExiste() {
        RestauranteJpaEntity entity = entityManager.persistFlushFind(novoRestauranteEntity("Cantina do Erick"));

        Optional<Restaurante> resultado = repository.findById(entity.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Cantina do Erick");
        assertThat(resultado.get().getDono().getId()).isEqualTo(dono.getId());
    }

    @Test
    void findById_deveRetornarVazio_quandoNaoExiste() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    void findAll_deveRetornarTodosOsRestaurantesCadastrados() {
        entityManager.persistAndFlush(novoRestauranteEntity("Cantina da Sophia"));
        entityManager.persistAndFlush(novoRestauranteEntity("Cantina do Erick"));

        List<Restaurante> resultado = repository.findAll();

        assertThat(resultado).hasSize(2);
    }

    @Test
    void delete_deveRemoverRestaurante() {
        RestauranteJpaEntity entity = entityManager.persistFlushFind(novoRestauranteEntity("Cantina da Sophia"));
        Restaurante restaurante = Restaurante.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .tipoCozinha(entity.getTipoCozinha())
                .horarioFuncionamento(entity.getHorarioFuncionamento())
                .dono(Usuario.builder().id(dono.getId()).build())
                .build();

        repository.delete(restaurante);

        assertThat(entityManager.find(RestauranteJpaEntity.class, entity.getId())).isNull();
    }
}
