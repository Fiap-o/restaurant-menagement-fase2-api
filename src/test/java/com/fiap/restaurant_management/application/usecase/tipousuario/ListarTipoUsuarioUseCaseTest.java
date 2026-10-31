package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.TipoUsuarioMapper;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ListarTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private TipoUsuarioMapper tipoUsuarioMapper;

    private ListarTipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarTipoUsuarioUseCase(tipoUsuarioRepository, tipoUsuarioMapper);
    }

    @Test
    void executar_deveRetornarListaDeTipos() {
        TipoUsuario cliente = new TipoUsuario();
        cliente.setId(1L);
        cliente.setNome("CLIENTE");
        TipoUsuario donoRestaurante = new TipoUsuario();
        donoRestaurante.setId(2L);
        donoRestaurante.setNome("DONO_RESTAURANTE");

        given(tipoUsuarioRepository.findAll()).willReturn(List.of(cliente, donoRestaurante));
        given(tipoUsuarioMapper.toResponseDTO(cliente))
                .willReturn(TipoUsuarioResponseDTO.builder().id(1L).nome("CLIENTE").build());
        given(tipoUsuarioMapper.toResponseDTO(donoRestaurante))
                .willReturn(TipoUsuarioResponseDTO.builder().id(2L).nome("DONO_RESTAURANTE").build());

        List<TipoUsuarioResponseDTO> resultado = useCase.executar();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(TipoUsuarioResponseDTO::getNome)
                .containsExactly("CLIENTE", "DONO_RESTAURANTE");
    }

    @Test
    void executar_deveRetornarListaVazia_quandoNaoHaTipos() {
        given(tipoUsuarioRepository.findAll()).willReturn(List.of());

        List<TipoUsuarioResponseDTO> resultado = useCase.executar();

        assertThat(resultado).isEmpty();
    }
}
