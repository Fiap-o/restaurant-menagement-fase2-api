package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.domain.exception.TipoUsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
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
class ExcluirTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    private ExcluirTipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ExcluirTipoUsuarioUseCase(tipoUsuarioRepository);
    }

    @Test
    void executar_deveExcluirComSucesso() {
        TipoUsuario existente = new TipoUsuario();
        existente.setId(1L);
        given(tipoUsuarioRepository.findById(1L)).willReturn(Optional.of(existente));

        useCase.executar(1L);

        verify(tipoUsuarioRepository, times(1)).delete(existente);
    }

    @Test
    void executar_deveLancarExcecao_quandoTipoNaoEncontrado() {
        given(tipoUsuarioRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(TipoUsuarioNaoEncontradoException.class);

        verify(tipoUsuarioRepository, never()).delete(any());
    }
}
