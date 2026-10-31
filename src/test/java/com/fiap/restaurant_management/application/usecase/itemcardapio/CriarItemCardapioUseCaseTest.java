package com.fiap.restaurant_management.application.usecase.itemcardapio;

import com.fiap.restaurant_management.application.dto.ItemCardapioCreateDTO;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CriarItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private ItemCardapioMapper itemCardapioMapper;

    private CriarItemCardapioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CriarItemCardapioUseCase(itemCardapioRepository, restauranteRepository, itemCardapioMapper);
    }

    private ItemCardapioCreateDTO dtoValido() {
        return new ItemCardapioCreateDTO(
                "Lasanha",
                "Massa, molho de tomate, carne moída e queijo",
                new BigDecimal("59.90"),
                false,
                "/fotos/lasanha.jpg",
                1L);
    }

    @Test
    void executar_deveCriarComSucesso_quandoRestauranteExiste() {
        ItemCardapioCreateDTO dto = dtoValido();
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);

        ItemCardapio mapeado = new ItemCardapio();
        mapeado.setNome(dto.getNome());

        given(restauranteRepository.findById(1L)).willReturn(Optional.of(restaurante));
        given(itemCardapioMapper.toEntity(dto)).willReturn(mapeado);
        given(itemCardapioRepository.save(any(ItemCardapio.class))).willAnswer(invocation -> {
            ItemCardapio item = invocation.getArgument(0);
            item.setId(1L);
            return item;
        });
        given(itemCardapioMapper.toResponseDTO(any(ItemCardapio.class)))
                .willReturn(ItemCardapioResponseDTO.builder().id(1L).nome(dto.getNome()).build());

        ItemCardapioResponseDTO resultado = useCase.executar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);

        ArgumentCaptor<ItemCardapio> captor = ArgumentCaptor.forClass(ItemCardapio.class);
        verify(itemCardapioRepository).save(captor.capture());
        assertThat(captor.getValue().getRestaurante()).isEqualTo(restaurante);
    }

    @Test
    void executar_deveLancarExcecao_quandoRestauranteNaoEncontrado() {
        ItemCardapioCreateDTO dto = dtoValido();
        given(restauranteRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(RestauranteNaoEncontradoException.class);

        verify(itemCardapioRepository, never()).save(any());
    }
}
