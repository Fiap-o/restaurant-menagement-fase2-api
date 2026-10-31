package com.fiap.restaurant_management.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.application.dto.RestauranteCreateDTO;
import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.application.dto.RestauranteUpdateDTO;
import com.fiap.restaurant_management.application.usecase.restaurante.AtualizarRestauranteUseCase;
import com.fiap.restaurant_management.application.usecase.restaurante.BuscarRestaurantePorIdUseCase;
import com.fiap.restaurant_management.application.usecase.restaurante.CriarRestauranteUseCase;
import com.fiap.restaurant_management.application.usecase.restaurante.ExcluirRestauranteUseCase;
import com.fiap.restaurant_management.application.usecase.restaurante.ListarRestauranteUseCase;
import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.infrastructure.security.JwtTokenService;
import com.fiap.restaurant_management.infrastructure.security.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RestauranteController.class)
@AutoConfigureMockMvc(addFilters = false)
class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CriarRestauranteUseCase criarRestauranteUseCase;

    @MockitoBean
    private AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    @MockitoBean
    private ExcluirRestauranteUseCase excluirRestauranteUseCase;

    @MockitoBean
    private BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase;

    @MockitoBean
    private ListarRestauranteUseCase listarRestauranteUseCase;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    private EnderecoDTO enderecoValido() {
        return new EnderecoDTO("Rua Coronel Joaquim Ferreira de Souza", "541", "São Paulo", "02419-070");
    }

    @Test
    void criar_deveRetornar201_quandoDonoExiste() throws Exception {
        RestauranteCreateDTO dto = new RestauranteCreateDTO(
                "Cantina da Sophia", enderecoValido(), "Italiana", "11:00-23:00", 1L);

        given(criarRestauranteUseCase.executar(any()))
                .willReturn(RestauranteResponseDTO.builder().id(1L).nome("Cantina da Sophia").build());

        mockMvc.perform(post("/api/v1/restaurantes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Cantina da Sophia"));
    }

    @Test
    void criar_deveRetornar404_quandoDonoNaoEncontrado() throws Exception {
        RestauranteCreateDTO dto = new RestauranteCreateDTO(
                "Cantina do Erick", enderecoValido(), "Italiana", "11:00-23:00", 99L);

        given(criarRestauranteUseCase.executar(any())).willThrow(new UsuarioNaoEncontradoException(99L));

        mockMvc.perform(post("/api/v1/restaurantes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizar_deveRetornar200_quandoRestauranteExiste() throws Exception {
        RestauranteUpdateDTO dto = new RestauranteUpdateDTO(
                "Cantina da Sophia Atualizada", enderecoValido(), "Italiana", "11:00-00:00", 1L);

        given(atualizarRestauranteUseCase.executar(eq(1L), any()))
                .willReturn(RestauranteResponseDTO.builder().id(1L).nome("Cantina da Sophia Atualizada").build());

        mockMvc.perform(put("/api/v1/restaurantes/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cantina da Sophia Atualizada"));
    }

    @Test
    void atualizar_deveRetornar404_quandoRestauranteNaoEncontrado() throws Exception {
        RestauranteUpdateDTO dto = new RestauranteUpdateDTO(
                "Cantina do Erick", enderecoValido(), "Italiana", "11:00-23:00", 1L);

        given(atualizarRestauranteUseCase.executar(eq(99L), any())).willThrow(new RestauranteNaoEncontradoException(99L));

        mockMvc.perform(put("/api/v1/restaurantes/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluir_deveRetornar204_quandoRestauranteExiste() throws Exception {
        mockMvc.perform(delete("/api/v1/restaurantes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluir_deveRetornar404_quandoRestauranteNaoEncontrado() throws Exception {
        willThrow(new RestauranteNaoEncontradoException(99L)).given(excluirRestauranteUseCase).executar(99L);

        mockMvc.perform(delete("/api/v1/restaurantes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorId_deveRetornar200_quandoRestauranteExiste() throws Exception {
        given(buscarRestaurantePorIdUseCase.executar(1L))
                .willReturn(RestauranteResponseDTO.builder().id(1L).nome("Cantina da Sophia").build());

        mockMvc.perform(get("/api/v1/restaurantes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Cantina da Sophia"));
    }

    @Test
    void buscarPorId_deveRetornar404_quandoRestauranteNaoEncontrado() throws Exception {
        given(buscarRestaurantePorIdUseCase.executar(99L)).willThrow(new RestauranteNaoEncontradoException(99L));

        mockMvc.perform(get("/api/v1/restaurantes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listar_deveRetornar200ComLista() throws Exception {
        given(listarRestauranteUseCase.executar())
                .willReturn(List.of(RestauranteResponseDTO.builder().id(1L).nome("Cantina da Sophia").build()));

        mockMvc.perform(get("/api/v1/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Cantina da Sophia"));
    }
}
