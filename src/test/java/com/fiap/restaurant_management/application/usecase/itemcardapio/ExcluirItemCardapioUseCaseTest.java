package com.fiap.restaurant_management.application.usecase.itemcardapio;

import com.fiap.restaurant_management.domain.exception.ItemCardapioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.repository.ItemCardapioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExcluirItemCardapioUseCaseTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    private ExcluirItemCardapioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ExcluirItemCardapioUseCase(itemCardapioRepository);
    }

    @Test
    void executar_deveExcluirComSucesso() {
        ItemCardapio existente = new ItemCardapio();
        existente.setId(1L);
        given(itemCardapioRepository.findById(1L)).willReturn(Optional.of(existente));

        useCase.executar(1L);

        verify(itemCardapioRepository, times(1)).delete(existente);
    }

    @Test
    void executar_deveLancarExcecao_quandoItemNaoEncontrado() {
        given(itemCardapioRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(ItemCardapioNaoEncontradoException.class);

        verify(itemCardapioRepository, never()).delete(any());
    }
}
