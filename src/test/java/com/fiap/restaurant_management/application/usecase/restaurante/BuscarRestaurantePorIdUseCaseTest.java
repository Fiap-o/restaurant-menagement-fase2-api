package com.fiap.restaurant_management.application.usecase.restaurante;

import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.application.mapper.RestauranteMapper;
import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
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
class BuscarRestaurantePorIdUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private RestauranteMapper restauranteMapper;

    private BuscarRestaurantePorIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarRestaurantePorIdUseCase(restauranteRepository, restauranteMapper);
    }

    @Test
    void executar_deveRetornarRestaurante_quandoEncontrado() {
        Restaurante existente = new Restaurante();
        existente.setId(1L);
        existente.setNome("Cantina da Nonna");

        given(restauranteRepository.findById(1L)).willReturn(Optional.of(existente));
        given(restauranteMapper.toResponseDTO(existente))
                .willReturn(RestauranteResponseDTO.builder().id(1L).nome("Cantina da Nonna").build());

        RestauranteResponseDTO resultado = useCase.executar(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Cantina da Nonna");
    }

    @Test
    void executar_deveLancarExcecao_quandoNaoEncontrado() {
        given(restauranteRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(99L))
                .isInstanceOf(RestauranteNaoEncontradoException.class);
    }
}
