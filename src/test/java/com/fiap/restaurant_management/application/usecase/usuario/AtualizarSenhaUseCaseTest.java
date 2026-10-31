package com.fiap.restaurant_management.application.usecase.usuario;

import com.fiap.restaurant_management.application.dto.SenhaUpdateDTO;
import com.fiap.restaurant_management.domain.exception.SenhaAtualInvalidaException;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AtualizarSenhaUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private AtualizarSenhaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarSenhaUseCase(usuarioRepository, passwordEncoder);
    }

    private Usuario usuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setSenha("hash-senha-atual");
        return usuario;
    }

    @Test
    void executar_deveAtualizarComSucesso_quandoSenhaAtualCorreta() {
        Usuario existente = usuarioExistente();
        SenhaUpdateDTO dto = new SenhaUpdateDTO("senhaAtual", "novaSenha");

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(passwordEncoder.matches("senhaAtual", "hash-senha-atual")).willReturn(true);
        given(passwordEncoder.encode("novaSenha")).willReturn("hash-nova-senha");

        useCase.executar(1L, dto);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getSenha()).isEqualTo("hash-nova-senha");
    }

    @Test
    void executar_deveLancarExcecao_quandoSenhaAtualIncorreta() {
        Usuario existente = usuarioExistente();
        SenhaUpdateDTO dto = new SenhaUpdateDTO("senhaErrada", "novaSenha");

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(passwordEncoder.matches("senhaErrada", "hash-senha-atual")).willReturn(false);

        assertThatThrownBy(() -> useCase.executar(1L, dto))
                .isInstanceOf(SenhaAtualInvalidaException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        given(usuarioRepository.findById(99L)).willReturn(Optional.empty());
        SenhaUpdateDTO dto = new SenhaUpdateDTO("qualquer", "novaSenha");

        assertThatThrownBy(() -> useCase.executar(99L, dto))
                .isInstanceOf(UsuarioNaoEncontradoException.class);

        verify(usuarioRepository, never()).save(any());
    }
}
