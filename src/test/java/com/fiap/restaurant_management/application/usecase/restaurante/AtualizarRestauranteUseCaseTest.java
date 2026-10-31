package com.fiap.restaurant_management.application.usecase.restaurante;

import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.application.dto.RestauranteUpdateDTO;
import com.fiap.restaurant_management.application.mapper.EnderecoMapper;
import com.fiap.restaurant_management.application.mapper.RestauranteMapper;
import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Endereco;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
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
class AtualizarRestauranteUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RestauranteMapper restauranteMapper;

    @Mock
    private EnderecoMapper enderecoMapper;

    private AtualizarRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarRestauranteUseCase(restauranteRepository, usuarioRepository, restauranteMapper, enderecoMapper);
    }

    private RestauranteUpdateDTO dtoValido() {
        return new RestauranteUpdateDTO(
                "Cantina da Nonna Atualizada",
                new EnderecoDTO("Rua Augusta", "500", "São Paulo", "01304-000"),
                "Italiana",
                "Todos os dias, 11h às 23h",
                1L);
    }

    @Test
    void executar_deveAtualizarComSucesso() {
        Restaurante existente = new Restaurante();
        existente.setId(1L);
        Usuario dono = new Usuario();
        dono.setId(1L);
        RestauranteUpdateDTO dto = dtoValido();

        given(restauranteRepository.findById(1L)).willReturn(Optional.of(existente));
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(dono));
        given(enderecoMapper.toEntity(dto.getEndereco()))
                .willReturn(new Endereco("Rua Augusta", "500", "São Paulo", "01304-000"));
        given(restauranteRepository.save(existente)).willReturn(existente);
        given(restauranteMapper.toResponseDTO(existente))
                .willReturn(RestauranteResponseDTO.builder().id(1L).nome(dto.getNome()).build());

        RestauranteResponseDTO resultado = useCase.executar(1L, dto);

        assertThat(resultado.getNome()).isEqualTo("Cantina da Nonna Atualizada");
        assertThat(existente.getDono()).isEqualTo(dono);
    }

    @Test
    void executar_deveLancarExcecao_quandoRestauranteNaoEncontrado() {
        given(restauranteRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L, dtoValido()))
                .isInstanceOf(RestauranteNaoEncontradoException.class);

        verify(restauranteRepository, never()).save(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoDonoNaoEncontrado() {
        Restaurante existente = new Restaurante();
        existente.setId(1L);
        given(restauranteRepository.findById(1L)).willReturn(Optional.of(existente));
        given(usuarioRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(1L, dtoValido()))
                .isInstanceOf(UsuarioNaoEncontradoException.class);

        verify(restauranteRepository, never()).save(any());
    }
}
