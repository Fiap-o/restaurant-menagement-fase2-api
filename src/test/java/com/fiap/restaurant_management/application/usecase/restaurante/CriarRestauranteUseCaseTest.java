package com.fiap.restaurant_management.application.usecase.restaurante;

import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.application.dto.RestauranteCreateDTO;
import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.application.mapper.RestauranteMapper;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
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
class CriarRestauranteUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RestauranteMapper restauranteMapper;

    private CriarRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CriarRestauranteUseCase(restauranteRepository, usuarioRepository, restauranteMapper);
    }

    private RestauranteCreateDTO dtoValido() {
        return new RestauranteCreateDTO(
                "Cantina da Nonna",
                new EnderecoDTO("Rua Augusta", "500", "São Paulo", "01304-000"),
                "Italiana",
                "Seg a Sáb, 11h às 23h",
                1L);
    }

    @Test
    void executar_deveCriarComSucesso_quandoDonoExiste() {
        RestauranteCreateDTO dto = dtoValido();
        Usuario dono = new Usuario();
        dono.setId(1L);
        dono.setNome("Erick Roberto Ribeiro");

        Restaurante mapeado = new Restaurante();
        mapeado.setNome(dto.getNome());

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(dono));
        given(restauranteMapper.toEntity(dto)).willReturn(mapeado);
        given(restauranteRepository.save(any(Restaurante.class))).willAnswer(invocation -> {
            Restaurante restaurante = invocation.getArgument(0);
            restaurante.setId(1L);
            return restaurante;
        });
        given(restauranteMapper.toResponseDTO(any(Restaurante.class)))
                .willReturn(RestauranteResponseDTO.builder().id(1L).nome(dto.getNome()).build());

        RestauranteResponseDTO resultado = useCase.executar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);

        ArgumentCaptor<Restaurante> captor = ArgumentCaptor.forClass(Restaurante.class);
        verify(restauranteRepository).save(captor.capture());
        assertThat(captor.getValue().getDono()).isEqualTo(dono);
    }

    @Test
    void executar_deveLancarExcecao_quandoDonoNaoEncontrado() {
        RestauranteCreateDTO dto = dtoValido();
        given(usuarioRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(UsuarioNaoEncontradoException.class);

        verify(restauranteRepository, never()).save(any());
    }
}
