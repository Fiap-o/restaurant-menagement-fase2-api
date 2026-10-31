package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.application.dto.TipoUsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.TipoUsuarioMapper;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioJaCadastradoException;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CriarTipoUsuarioUseCaseTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private TipoUsuarioMapper tipoUsuarioMapper;

    private CriarTipoUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CriarTipoUsuarioUseCase(tipoUsuarioRepository, tipoUsuarioMapper);
    }

    @Test
    void executar_deveCriarComSucesso_quandoNomeDisponivel() {
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("CLIENTE");
        TipoUsuario mapeado = new TipoUsuario();
        mapeado.setNome("CLIENTE");
        TipoUsuario salvo = new TipoUsuario();
        salvo.setId(1L);
        salvo.setNome("CLIENTE");

        given(tipoUsuarioRepository.existsByNome("CLIENTE")).willReturn(false);
        given(tipoUsuarioMapper.toEntity(dto)).willReturn(mapeado);
        given(tipoUsuarioRepository.save(mapeado)).willReturn(salvo);
        given(tipoUsuarioMapper.toResponseDTO(salvo))
                .willReturn(TipoUsuarioResponseDTO.builder().id(1L).nome("CLIENTE").build());

        TipoUsuarioResponseDTO resultado = useCase.executar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("CLIENTE");
    }

    @Test
    void executar_deveLancarExcecao_quandoNomeJaCadastrado() {
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("CLIENTE");
        given(tipoUsuarioRepository.existsByNome("CLIENTE")).willReturn(true);

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(TipoUsuarioJaCadastradoException.class)
                .hasMessageContaining("CLIENTE");

        org.mockito.Mockito.verify(tipoUsuarioRepository, never()).save(any());
    }
}
