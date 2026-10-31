package com.fiap.restaurant_management.application.usecase.restaurante;

import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
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
class ExcluirRestauranteUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    private ExcluirRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ExcluirRestauranteUseCase(restauranteRepository);
    }

    @Test
    void executar_deveExcluirComSucesso() {
        Restaurante existente = new Restaurante();
        existente.setId(1L);
        given(restauranteRepository.findById(1L)).willReturn(Optional.of(existente));

        useCase.executar(1L);

        verify(restauranteRepository, times(1)).delete(existente);
    }

    @Test
    void executar_deveLancarExcecao_quandoRestauranteNaoEncontrado() {
        given(restauranteRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(RestauranteNaoEncontradoException.class);

        verify(restauranteRepository, never()).delete(any());
    }
}
