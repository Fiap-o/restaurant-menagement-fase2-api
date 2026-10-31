package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.TipoUsuarioMapper;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
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
class BuscarTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private TipoUsuarioMapper tipoUsuarioMapper;

    private BuscarTipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarTipoUsuarioUseCase(tipoUsuarioRepository, tipoUsuarioMapper);
    }

    @Test
    void executar_deveRetornarTipoUsuario_quandoEncontrado() {
        TipoUsuario existente = new TipoUsuario();
        existente.setId(1L);
        existente.setNome("CLIENTE");

        given(tipoUsuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(tipoUsuarioMapper.toResponseDTO(existente))
                .willReturn(TipoUsuarioResponseDTO.builder().id(1L).nome("CLIENTE").build());

        TipoUsuarioResponseDTO resultado = useCase.executar(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("CLIENTE");
    }

    @Test
    void executar_deveLancarExcecao_quandoNaoEncontrado() {
        given(tipoUsuarioRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(TipoUsuarioNaoEncontradoException.class);
    }
}
