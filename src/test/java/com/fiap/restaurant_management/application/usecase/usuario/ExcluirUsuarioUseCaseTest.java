package com.fiap.restaurant_management.application.usecase.usuario;

import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
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
class ExcluirUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private ExcluirUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ExcluirUsuarioUseCase(usuarioRepository);
    }

    @Test
    void executar_deveExcluirComSucesso() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));

        useCase.executar(1L);

        verify(usuarioRepository, times(1)).delete(existente);
    }

    @Test
    void executar_deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        given(usuarioRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(UsuarioNaoEncontradoException.class);

        verify(usuarioRepository, never()).delete(any());
    }
}
