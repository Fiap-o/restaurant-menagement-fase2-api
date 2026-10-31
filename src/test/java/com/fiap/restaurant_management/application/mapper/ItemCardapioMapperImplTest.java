package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.ItemCardapioCreateDTO;
import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.model.Restaurante;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ItemCardapioMapperImplTest {

    private final ItemCardapioMapperImpl mapper = new ItemCardapioMapperImpl();

    @Test
    void toEntity_deveConverterDtoParaDominio() {
        ItemCardapioCreateDTO dto = new ItemCardapioCreateDTO(
                "Lasanha", "Massa, molho de tomate, carne moída e queijo",
                new BigDecimal("59.90"), false, "/fotos/lasanha.jpg", 1L);

        ItemCardapio item = mapper.toEntity(dto);

        assertThat(item.getNome()).isEqualTo("Lasanha");
        assertThat(item.getDescricao()).isEqualTo("Massa, molho de tomate, carne moída e queijo");
        assertThat(item.getPreco()).isEqualByComparingTo("59.90");
        assertThat(item.isDisponivelSomenteNoLocal()).isFalse();
        assertThat(item.getCaminhoFoto()).isEqualTo("/fotos/lasanha.jpg");
    }

    @Test
    void toResponseDTO_deveConverterDominioParaDto_comRestaurante() {
        Restaurante restaurante = Restaurante.builder().id(10L).build();
        ItemCardapio item = ItemCardapio.builder()
                .id(1L)
                .nome("Lasanha")
                .descricao("Massa, molho de tomate, carne moída e queijo")
                .preco(new BigDecimal("59.90"))
                .disponivelSomenteNoLocal(false)
                .caminhoFoto("/fotos/lasanha.jpg")
                .restaurante(restaurante)
                .build();

        ItemCardapioResponseDTO dto = mapper.toResponseDTO(item);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNome()).isEqualTo("Lasanha");
        assertThat(dto.getRestauranteId()).isEqualTo(10L);
    }

    @Test
    void toResponseDTO_deveRetornarRestauranteIdNull_quandoRestauranteNull() {
        ItemCardapio item = ItemCardapio.builder()
                .id(1L)
                .nome("Lasanha")
                .restaurante(null)
                .build();

        ItemCardapioResponseDTO dto = mapper.toResponseDTO(item);

        assertThat(dto.getRestauranteId()).isNull();
    }
}
