package com.fiap.restaurant_management.application.usecase.itemcardapio;

import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.application.mapper.ItemCardapioMapper;
import com.fiap.restaurant_management.domain.exception.ItemCardapioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.repository.ItemCardapioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BuscarItemCardapioPorIdUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private ItemCardapioMapper itemCardapioMapper;

    private BuscarItemCardapioPorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarItemCardapioPorIdUseCase(itemCardapioRepository, itemCardapioMapper);
    }

    @Test
    void executar_deveRetornarItem_quandoEncontrado() {
        ItemCardapio existente = new ItemCardapio();
        existente.setId(1L);
        existente.setNome("Lasanha");

        given(itemCardapioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(itemCardapioMapper.toResponseDTO(existente))
                .willReturn(ItemCardapioResponseDTO.builder().id(1L).nome("Lasanha").build());

        ItemCardapioResponseDTO resultado = useCase.executar(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Lasanha");
    }

    @Test
    void executar_deveLancarExcecao_quandoNaoEncontrado() {
        given(itemCardapioRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(ItemCardapioNaoEncontradoException.class);
    }
}
