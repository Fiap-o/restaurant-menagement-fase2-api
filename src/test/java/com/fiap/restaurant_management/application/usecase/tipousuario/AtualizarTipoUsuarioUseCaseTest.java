package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.application.dto.TipoUsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.TipoUsuarioMapper;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioJaCadastradoException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AtualizarTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private TipoUsuarioMapper tipoUsuarioMapper;

    private AtualizarTipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarTipoUsuarioUseCase(tipoUsuarioRepository, tipoUsuarioMapper);
    }

    private TipoUsuario tipoExistente() {
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(1L);
        tipoUsuario.setNome("CLIENTE");
        return tipoUsuario;
    }

    @Test
    void executar_deveAtualizarComSucesso() {
        TipoUsuario existente = tipoExistente();
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("CLIENTE_VIP");

        given(tipoUsuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(tipoUsuarioRepository.existsByNome("CLIENTE_VIP")).willReturn(false);
        given(tipoUsuarioRepository.save(existente)).willReturn(existente);
        given(tipoUsuarioMapper.toResponseDTO(existente))
                .willReturn(TipoUsuarioResponseDTO.builder().id(1L).nome("CLIENTE_VIP").build());

        TipoUsuarioResponseDTO resultado = useCase.executar(1L, dto);

        assertThat(resultado.getNome()).isEqualTo("CLIENTE_VIP");
        assertThat(existente.getNome()).isEqualTo("CLIENTE_VIP");
    }

    @Test
    void executar_naoDeveValidarDuplicidade_quandoNomeNaoMuda() {
        TipoUsuario existente = tipoExistente();
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("CLIENTE");

        given(tipoUsuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(tipoUsuarioRepository.save(existente)).willReturn(existente);
        given(tipoUsuarioMapper.toResponseDTO(existente))
                .willReturn(TipoUsuarioResponseDTO.builder().id(1L).nome("CLIENTE").build());

        useCase.executar(1L, dto);

        verify(tipoUsuarioRepository, never()).existsByNome(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoTipoNaoEncontrado() {
        given(tipoUsuarioRepository.findById(99L)).willReturn(Optional.empty());
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("QUALQUER");

        assertThatThrownBy(() -> useCase.executar(99L, dto))
                .isInstanceOf(TipoUsuarioNaoEncontradoException.class);

        verify(tipoUsuarioRepository, never()).save(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoNovoNomeJaCadastrado() {
        TipoUsuario existente = tipoExistente();
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("DONO_RESTAURANTE");

        given(tipoUsuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(tipoUsuarioRepository.existsByNome("DONO_RESTAURANTE")).willReturn(true);

        assertThatThrownBy(() -> useCase.executar(1L, dto))
                .isInstanceOf(TipoUsuarioJaCadastradoException.class);

        verify(tipoUsuarioRepository, never()).save(any());
    }
}
