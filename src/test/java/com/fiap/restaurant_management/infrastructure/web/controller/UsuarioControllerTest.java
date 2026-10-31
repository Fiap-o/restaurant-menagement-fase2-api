package com.fiap.restaurant_management.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.application.dto.SenhaUpdateDTO;
import com.fiap.restaurant_management.application.dto.UsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.application.dto.UsuarioUpdateDTO;
import com.fiap.restaurant_management.application.usecase.usuario.AtualizarSenhaUseCase;
import com.fiap.restaurant_management.application.usecase.usuario.AtualizarUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.usuario.BuscarUsuarioPorIdUseCase;
import com.fiap.restaurant_management.application.usecase.usuario.BuscarUsuarioPorNomeUseCase;
import com.fiap.restaurant_management.application.usecase.usuario.CriarUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.usuario.ExcluirUsuarioUseCase;
import com.fiap.restaurant_management.domain.exception.EmailJaCadastradoException;
import com.fiap.restaurant_management.domain.exception.SenhaAtualInvalidaException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CriarUsuarioUseCase criarUsuarioUseCase;

    @MockitoBean
    private AtualizarUsuarioUseCase atualizarUsuarioUseCase;

    @MockitoBean
    private AtualizarSenhaUseCase atualizarSenhaUseCase;

    @MockitoBean
    private ExcluirUsuarioUseCase excluirUsuarioUseCase;

    @MockitoBean
    private BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase;

    @MockitoBean
    private BuscarUsuarioPorNomeUseCase buscarUsuarioPorNomeUseCase;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    private EnderecoDTO enderecoValido() {
        return new EnderecoDTO("Rua Coronel Joaquim Ferreira de Souza", "541", "São Paulo", "02419-070");
    }

    @Test
    void criar_deveRetornar201_quandoDadosValidos() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO(
                "Sophia Amaral Silva", "sophia.amaral@fiap.com.br", "sophia.amaral", "senhaForte123", 1L, enderecoValido());

        given(criarUsuarioUseCase.executar(any()))
                .willReturn(UsuarioResponseDTO.builder().id(1L).nome("Sophia Amaral Silva").build());

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Sophia Amaral Silva"));
    }

    @Test
    void criar_deveRetornar409_quandoEmailJaCadastrado() throws Exception {
        UsuarioCreateDTO dto = new UsuarioCreateDTO(
                "Erick Roberto Ribeiro", "erick.ribeiro@fiap.com.br", "erick.ribeiro", "senhaForte123", 1L, enderecoValido());

        given(criarUsuarioUseCase.executar(any())).willThrow(new EmailJaCadastradoException("erick.ribeiro@fiap.com.br"));

        mockMvc.perform(post("/api/v1/usuarios")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void atualizarDados_deveRetornar200_quandoUsuarioExiste() throws Exception {
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(
                "Sophia Amaral Silva", "sophia.amaral@fiap.com.br", "sophia.amaral", enderecoValido());

        given(atualizarUsuarioUseCase.executar(eq(1L), any()))
                .willReturn(UsuarioResponseDTO.builder().id(1L).nome("Sophia Amaral Silva").build());

        mockMvc.perform(put("/api/v1/usuarios/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Sophia Amaral Silva"));
    }

    @Test
    void atualizarDados_deveRetornar404_quandoUsuarioNaoEncontrado() throws Exception {
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(
                "Erick Roberto Ribeiro", "erick.ribeiro@fiap.com.br", "erick.ribeiro", enderecoValido());

        given(atualizarUsuarioUseCase.executar(eq(99L), any())).willThrow(new UsuarioNaoEncontradoException(99L));

        mockMvc.perform(put("/api/v1/usuarios/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarSenha_deveRetornar204_quandoSenhaAtualCorreta() throws Exception {
        SenhaUpdateDTO dto = new SenhaUpdateDTO("senhaForte123", "novaSenhaForte456");

        mockMvc.perform(patch("/api/v1/usuarios/1/senha")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void atualizarSenha_deveRetornar400_quandoSenhaAtualInvalida() throws Exception {
        SenhaUpdateDTO dto = new SenhaUpdateDTO("senhaErrada", "novaSenhaForte456");

        willThrow(new SenhaAtualInvalidaException())
                .given(atualizarSenhaUseCase).executar(eq(1L), any());

        mockMvc.perform(patch("/api/v1/usuarios/1/senha")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void excluir_deveRetornar204_quandoUsuarioExiste() throws Exception {
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluir_deveRetornar404_quandoUsuarioNaoEncontrado() throws Exception {
        willThrow(new UsuarioNaoEncontradoException(99L)).given(excluirUsuarioUseCase).executar(99L);

        mockMvc.perform(delete("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorId_deveRetornar200_quandoUsuarioExiste() throws Exception {
        given(buscarUsuarioPorIdUseCase.executar(1L))
                .willReturn(UsuarioResponseDTO.builder().id(1L).nome("Sophia Amaral Silva").build());

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Sophia Amaral Silva"));
    }

    @Test
    void buscarPorId_deveRetornar404_quandoUsuarioNaoEncontrado() throws Exception {
        given(buscarUsuarioPorIdUseCase.executar(99L)).willThrow(new UsuarioNaoEncontradoException(99L));

        mockMvc.perform(get("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorNome_deveRetornar200ComLista() throws Exception {
        given(buscarUsuarioPorNomeUseCase.executar("Amaral"))
                .willReturn(List.of(UsuarioResponseDTO.builder().id(1L).nome("Sophia Amaral Silva").build()));

        mockMvc.perform(get("/api/v1/usuarios").param("nome", "Amaral"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Sophia Amaral Silva"));
    }
}
