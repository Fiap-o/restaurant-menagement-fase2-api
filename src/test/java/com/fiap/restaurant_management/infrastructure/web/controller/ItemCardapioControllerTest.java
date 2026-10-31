package com.fiap.restaurant_management.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.restaurant_management.application.dto.ItemCardapioCreateDTO;
import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.application.dto.ItemCardapioUpdateDTO;
import com.fiap.restaurant_management.application.usecase.itemcardapio.AtualizarItemCardapioUseCase;
import com.fiap.restaurant_management.application.usecase.itemcardapio.BuscarItemCardapioPorIdUseCase;
import com.fiap.restaurant_management.application.usecase.itemcardapio.CriarItemCardapioUseCase;
import com.fiap.restaurant_management.application.usecase.itemcardapio.ExcluirItemCardapioUseCase;
import com.fiap.restaurant_management.application.usecase.itemcardapio.ListarItemCardapioPorRestauranteUseCase;
import com.fiap.restaurant_management.domain.exception.ItemCardapioNaoEncontradoException;
import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.infrastructure.security.JwtTokenService;
import com.fiap.restaurant_management.infrastructure.security.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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

@WebMvcTest(controllers = ItemCardapioController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemCardapioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CriarItemCardapioUseCase criarItemCardapioUseCase;

    @MockitoBean
    private AtualizarItemCardapioUseCase atualizarItemCardapioUseCase;

    @MockitoBean
    private ExcluirItemCardapioUseCase excluirItemCardapioUseCase;

    @MockitoBean
    private BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase;

    @MockitoBean
    private ListarItemCardapioPorRestauranteUseCase listarItemCardapioPorRestauranteUseCase;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    @Test
    void criar_deveRetornar201_quandoRestauranteExiste() throws Exception {
        ItemCardapioCreateDTO dto = new ItemCardapioCreateDTO(
                "Lasanha", "Massa, molho de tomate, carne moída e queijo",
                new BigDecimal("59.90"), false, "/fotos/lasanha.jpg", 1L);

        given(criarItemCardapioUseCase.executar(any()))
                .willReturn(ItemCardapioResponseDTO.builder().id(1L).nome("Lasanha").build());

        mockMvc.perform(post("/api/v1/itens-cardapio")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Lasanha"));
    }

    @Test
    void criar_deveRetornar404_quandoRestauranteNaoEncontrado() throws Exception {
        ItemCardapioCreateDTO dto = new ItemCardapioCreateDTO(
                "Estrogonofe", "Carne em tiras, creme de leite e batata palha",
                new BigDecimal("29.90"), false, null, 99L);

        given(criarItemCardapioUseCase.executar(any())).willThrow(new RestauranteNaoEncontradoException(99L));

        mockMvc.perform(post("/api/v1/itens-cardapio")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void criar_deveRetornar400_quandoPrecoNegativo() throws Exception {
        ItemCardapioCreateDTO dto = new ItemCardapioCreateDTO(
                "Lasanha", "Massa, molho de tomate, carne moída e queijo",
                new BigDecimal("-10"), false, null, 1L);

        mockMvc.perform(post("/api/v1/itens-cardapio")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void atualizar_deveRetornar200_quandoItemExiste() throws Exception {
        ItemCardapioUpdateDTO dto = new ItemCardapioUpdateDTO(
                "Lasanha Especial", "Nova descrição", new BigDecimal("69.90"), true, "/fotos/novo.jpg");

        given(atualizarItemCardapioUseCase.executar(eq(1L), any()))
                .willReturn(ItemCardapioResponseDTO.builder().id(1L).nome("Lasanha Especial").build());

        mockMvc.perform(put("/api/v1/itens-cardapio/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Lasanha Especial"));
    }

    @Test
    void atualizar_deveRetornar404_quandoItemNaoEncontrado() throws Exception {
        ItemCardapioUpdateDTO dto = new ItemCardapioUpdateDTO(
                "Estrogonofe", "Carne em tiras, creme de leite e batata palha", new BigDecimal("29.90"), false, null);

        given(atualizarItemCardapioUseCase.executar(eq(99L), any())).willThrow(new ItemCardapioNaoEncontradoException(99L));

        mockMvc.perform(put("/api/v1/itens-cardapio/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluir_deveRetornar204_quandoItemExiste() throws Exception {
        mockMvc.perform(delete("/api/v1/itens-cardapio/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluir_deveRetornar404_quandoItemNaoEncontrado() throws Exception {
        willThrow(new ItemCardapioNaoEncontradoException(99L)).given(excluirItemCardapioUseCase).executar(99L);

        mockMvc.perform(delete("/api/v1/itens-cardapio/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorId_deveRetornar200_quandoItemExiste() throws Exception {
        given(buscarItemCardapioPorIdUseCase.executar(1L))
                .willReturn(ItemCardapioResponseDTO.builder().id(1L).nome("Lasanha").build());

        mockMvc.perform(get("/api/v1/itens-cardapio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Lasanha"));
    }

    @Test
    void buscarPorId_deveRetornar404_quandoItemNaoEncontrado() throws Exception {
        given(buscarItemCardapioPorIdUseCase.executar(99L)).willThrow(new ItemCardapioNaoEncontradoException(99L));

        mockMvc.perform(get("/api/v1/itens-cardapio/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarPorRestaurante_deveRetornar200ComLista() throws Exception {
        given(listarItemCardapioPorRestauranteUseCase.executar(1L))
                .willReturn(List.of(ItemCardapioResponseDTO.builder().id(1L).nome("Lasanha").build()));

        mockMvc.perform(get("/api/v1/itens-cardapio/restaurante/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Lasanha"));
    }

    @Test
    void listarPorRestaurante_deveRetornar404_quandoRestauranteNaoEncontrado() throws Exception {
        given(listarItemCardapioPorRestauranteUseCase.executar(99L)).willThrow(new RestauranteNaoEncontradoException(99L));

        mockMvc.perform(get("/api/v1/itens-cardapio/restaurante/99"))
                .andExpect(status().isNotFound());
    }
}
