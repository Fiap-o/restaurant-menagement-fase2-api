package com.fiap.restaurant_management.application.usecase.restaurante;

import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.application.mapper.RestauranteMapper;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ListarRestauranteUseCaseTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    @Mock
    private RestauranteMapper restauranteMapper;

    private ListarRestauranteUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarRestauranteUseCase(restauranteRepository, restauranteMapper);
    }

    @Test
    void executar_deveRetornarListaDeRestaurantes() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Cantina da Nonna");

        given(restauranteRepository.findAll()).willReturn(List.of(restaurante));
        given(restauranteMapper.toResponseDTO(restaurante))
                .willReturn(RestauranteResponseDTO.builder().id(1L).nome("Cantina da Nonna").build());

        List<RestauranteResponseDTO> resultado = useCase.executar();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Cantina da Nonna");
    }

    @Test
    void executar_deveRetornarListaVazia_quandoNaoHaRestaurantes() {
        given(restauranteRepository.findAll()).willReturn(List.of());

        List<RestauranteResponseDTO> resultado = useCase.executar();

        assertThat(resultado).isEmpty();
    }
}
