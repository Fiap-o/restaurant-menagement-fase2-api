package com.fiap.restaurant_management.application.usecase.usuario;

import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.UsuarioMapper;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
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
class BuscarUsuarioPorIdUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    private BuscarUsuarioPorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarUsuarioPorIdUseCase(usuarioRepository, usuarioMapper);
    }

    @Test
    void executar_deveRetornarUsuario_quandoEncontrado() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setLogin("sophia.asilva");

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(usuarioMapper.toResponseDTO(existente))
                .willReturn(UsuarioResponseDTO.builder().id(1L).login("sophia.asilva").build());

        UsuarioResponseDTO resultado = useCase.executar(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getLogin()).isEqualTo("sophia.asilva");
    }

    @Test
    void executar_deveLancarExcecao_quandoNaoEncontrado() {
        given(usuarioRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(UsuarioNaoEncontradoException.class);
    }
}
