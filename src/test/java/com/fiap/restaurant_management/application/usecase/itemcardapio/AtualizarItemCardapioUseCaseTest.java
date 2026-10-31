package com.fiap.restaurant_management.application.usecase.itemcardapio;

import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.application.dto.ItemCardapioUpdateDTO;
import com.fiap.restaurant_management.application.mapper.ItemCardapioMapper;
import com.fiap.restaurant_management.domain.exception.ItemCardapioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.repository.ItemCardapioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class AtualizarItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private ItemCardapioMapper itemCardapioMapper;

    private AtualizarItemCardapioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarItemCardapioUseCase(itemCardapioRepository, itemCardapioMapper);
    }

    @Test
    void executar_deveAtualizarComSucesso() {
        ItemCardapio existente = new ItemCardapio();
        existente.setId(1L);
        ItemCardapioUpdateDTO dto = new ItemCardapioUpdateDTO(
                "Lasanha Especial", "Nova descrição", new BigDecimal("69.90"), true, "/fotos/novo.jpg");

        given(itemCardapioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(itemCardapioRepository.save(existente)).willReturn(existente);
        given(itemCardapioMapper.toResponseDTO(existente))
                .willReturn(ItemCardapioResponseDTO.builder().id(1L).nome(dto.getNome()).build());

        ItemCardapioResponseDTO resultado = useCase.executar(1L, dto);

        assertThat(resultado.getNome()).isEqualTo("Lasanha Especial");
        assertThat(existente.getPreco()).isEqualByComparingTo("69.90");
        assertThat(existente.isDisponivelSomenteNoLocal()).isTrue();
    }

    @Test
    void executar_deveLancarExcecao_quandoItemNaoEncontrado() {
        given(itemCardapioRepository.findById(99L)).willReturn(Optional.empty());
        ItemCardapioUpdateDTO dto = new ItemCardapioUpdateDTO(
                "Nome", "Descrição", new BigDecimal("10"), false, null);

        assertThatThrownBy(() -> useCase.executar(99L, dto))
                .isInstanceOf(ItemCardapioNaoEncontradoException.class);

        verify(itemCardapioRepository, never()).save(any());
    }
}
