package com.fiap.restaurant_management.infrastructure.persistence.repository;

import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.infrastructure.persistence.entity.ItemCardapioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.entity.RestauranteJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.entity.TipoUsuarioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.entity.UsuarioJpaEntity;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.EnderecoEntityMapper;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.ItemCardapioEntityMapper;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.RestauranteEntityMapper;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.TipoUsuarioEntityMapper;
import com.fiap.restaurant_management.infrastructure.persistence.mapper.UsuarioEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        ItemCardapioRepositoryImpl.class, ItemCardapioEntityMapper.class, RestauranteEntityMapper.class,
        UsuarioEntityMapper.class, TipoUsuarioEntityMapper.class, EnderecoEntityMapper.class
})
class ItemCardapioRepositoryImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemCardapioRepositoryImpl repository;

    private RestauranteJpaEntity restaurante;

    @BeforeEach
    void setUp() {
        TipoUsuarioJpaEntity tipoUsuario = entityManager.persistFlushFind(
                TipoUsuarioJpaEntity.builder().nome("Dono de Restaurante").build());

        UsuarioJpaEntity dono = entityManager.persistFlushFind(UsuarioJpaEntity.builder()
                .nome("Erick Roberto Ribeiro")
                .email("erick.ribeiro@fiap.com.br")
                .login("erick.ribeiro")
                .senha("senha-criptografada")
                .tipoUsuario(tipoUsuario)
                .dataUltimaAlteracao(LocalDateTime.now())
                .build());

        restaurante = entityManager.persistFlushFind(RestauranteJpaEntity.builder()
                .nome("Cantina do Erick")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("11:00-23:00")
                .dono(dono)
                .build());
    }

    private ItemCardapioJpaEntity novoItemEntity(String nome) {
        return ItemCardapioJpaEntity.builder()
                .nome(nome)
                .descricao("Massa, molho de tomate, carne moída e queijo")
                .preco(new BigDecimal("59.90"))
                .disponivelSomenteNoLocal(false)
                .caminhoFoto("/fotos/lasanha.jpg")
                .restaurante(restaurante)
                .build();
    }

    @Test
    void save_devePersistirEGerarId() {
        ItemCardapio item = ItemCardapio.builder()
                .nome("Lasanha")
                .descricao("Massa, molho de tomate, carne moída e queijo")
                .preco(new BigDecimal("59.90"))
                .disponivelSomenteNoLocal(false)
                .caminhoFoto("/fotos/lasanha.jpg")
                .restaurante(Restaurante.builder().id(restaurante.getId()).nome(restaurante.getNome()).build())
                .build();

        ItemCardapio salvo = repository.save(item);

        assertThat(salvo.getId()).isNotNull();
        assertThat(entityManager.find(ItemCardapioJpaEntity.class, salvo.getId()).getNome())
                .isEqualTo("Lasanha");
    }

    @Test
    void findById_deveRetornarItem_quandoExiste() {
        ItemCardapioJpaEntity entity = entityManager.persistFlushFind(novoItemEntity("Lasanha"));

        Optional<ItemCardapio> resultado = repository.findById(entity.getId());

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Lasanha");
        assertThat(resultado.get().getRestaurante().getId()).isEqualTo(restaurante.getId());
    }

    @Test
    void findById_deveRetornarVazio_quandoNaoExiste() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    void findByRestauranteId_deveRetornarItensDoRestaurante() {
        entityManager.persistAndFlush(novoItemEntity("Lasanha"));
        entityManager.persistAndFlush(novoItemEntity("Macarrão à Bolonhesa"));

        List<ItemCardapio> resultado = repository.findByRestauranteId(restaurante.getId());

        assertThat(resultado).hasSize(2);
    }

    @Test
    void findByRestauranteId_deveRetornarListaVazia_quandoRestauranteNaoTemItens() {
        List<ItemCardapio> resultado = repository.findByRestauranteId(restaurante.getId());

        assertThat(resultado).isEmpty();
    }

    @Test
    void delete_deveRemoverItem() {
        ItemCardapioJpaEntity entity = entityManager.persistFlushFind(novoItemEntity("Lasanha"));
        ItemCardapio item = ItemCardapio.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .preco(entity.getPreco())
                .disponivelSomenteNoLocal(entity.isDisponivelSomenteNoLocal())
                .caminhoFoto(entity.getCaminhoFoto())
                .restaurante(Restaurante.builder().id(restaurante.getId()).build())
                .build();

        repository.delete(item);

        assertThat(entityManager.find(ItemCardapioJpaEntity.class, entity.getId())).isNull();
    }
}
