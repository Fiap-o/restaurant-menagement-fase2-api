package com.fiap.restaurant_management.application.usecase.usuario;

import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.UsuarioMapper;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BuscarUsuarioPorNomeUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    private BuscarUsuarioPorNomeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarUsuarioPorNomeUseCase(usuarioRepository, usuarioMapper);
    }

    @Test
    void executar_deveRetornarListaDeUsuarios() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setNome("Sophia Amaral Silva");

        given(usuarioRepository.findByNomeContainingIgnoreCase("Sophia")).willReturn(List.of(existente));
        given(usuarioMapper.toResponseDTO(existente))
                .willReturn(UsuarioResponseDTO.builder().id(1L).nome("Sophia Amaral Silva").build());

        List<UsuarioResponseDTO> resultado = useCase.executar("Sophia");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Sophia Amaral Silva");
    }

    @Test
    void executar_deveRetornarListaVazia_quandoNenhumUsuarioEncontrado() {
        given(usuarioRepository.findByNomeContainingIgnoreCase("Inexistente")).willReturn(List.of());

        List<UsuarioResponseDTO> resultado = useCase.executar("Inexistente");

        assertThat(resultado).isEmpty();
    }
}
