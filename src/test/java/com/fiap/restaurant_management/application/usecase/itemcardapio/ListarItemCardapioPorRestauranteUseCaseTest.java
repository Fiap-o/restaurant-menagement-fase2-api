package com.fiap.restaurant_management.application.usecase.itemcardapio;

import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.application.mapper.ItemCardapioMapper;
import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.repository.ItemCardapioRepository;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ListarItemCardapioPorRestauranteUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ItemCardapioMapper itemCardapioMapper;

    private ListarItemCardapioPorRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarItemCardapioPorRestauranteUseCase(
                itemCardapioRepository, restauranteRepository, itemCardapioMapper);
    }

    @Test
    void executar_deveRetornarListaDeItens_quandoRestauranteExiste() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        ItemCardapio item = new ItemCardapio();
        item.setId(1L);
        item.setNome("Lasanha");

        given(restauranteRepository.findById(1L)).willReturn(Optional.of(restaurante));
        given(itemCardapioRepository.findByRestauranteId(1L)).willReturn(List.of(item));
        given(itemCardapioMapper.toResponseDTO(item))
                .willReturn(ItemCardapioResponseDTO.builder().id(1L).nome("Lasanha").build());

        List<ItemCardapioResponseDTO> resultado = useCase.executar(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Lasanha");
    }

    @Test
    void executar_deveRetornarListaVazia_quandoRestauranteNaoTemItens() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        given(restauranteRepository.findById(1L)).willReturn(Optional.of(restaurante));
        given(itemCardapioRepository.findByRestauranteId(1L)).willReturn(List.of());

        List<ItemCardapioResponseDTO> resultado = useCase.executar(1L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void executar_deveLancarExcecao_quandoRestauranteNaoEncontrado() {
        given(restauranteRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(RestauranteNaoEncontradoException.class);
    }
}
