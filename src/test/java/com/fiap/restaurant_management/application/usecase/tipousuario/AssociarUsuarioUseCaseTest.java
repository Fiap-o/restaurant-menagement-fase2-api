package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.UsuarioMapper;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AssociarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    private AssociarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AssociarUsuarioUseCase(usuarioRepository, tipoUsuarioRepository, usuarioMapper);
    }

    @Test
    void executar_deveAssociarTipoAoUsuario_comSucesso() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        TipoUsuario novoTipo = new TipoUsuario();
        novoTipo.setId(2L);
        novoTipo.setNome("DONO_RESTAURANTE");

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario));
        given(tipoUsuarioRepository.findById(2L)).willReturn(Optional.of(novoTipo));
        given(usuarioRepository.save(usuario)).willReturn(usuario);
        given(usuarioMapper.toResponseDTO(usuario))
                .willReturn(UsuarioResponseDTO.builder().id(1L).build());

        useCase.executar(1L, 2L);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getTipoUsuario()).isEqualTo(novoTipo);
    }

    @Test
    void executar_deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        given(usuarioRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L, 1L))
                .isInstanceOf(UsuarioNaoEncontradoException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoTipoUsuarioNaoEncontrado() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario));
        given(tipoUsuarioRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(1L, 99L))
                .isInstanceOf(TipoUsuarioNaoEncontradoException.class);

        verify(usuarioRepository, never()).save(any());
    }
}
