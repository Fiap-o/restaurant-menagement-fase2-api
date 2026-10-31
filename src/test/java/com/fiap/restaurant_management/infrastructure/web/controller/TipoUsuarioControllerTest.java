package com.fiap.restaurant_management.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.restaurant_management.application.dto.TipoUsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.application.usecase.tipousuario.AssociarUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.tipousuario.AtualizarTipoUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.tipousuario.BuscarTipoUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.tipousuario.CriarTipoUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.tipousuario.ExcluirTipoUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.tipousuario.ListarTipoUsuarioUseCase;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioJaCadastradoException;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioNaoEncontradoException;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TipoUsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class TipoUsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CriarTipoUsuarioUseCase criarTipoUsuarioUseCase;

    @MockitoBean
    private AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase;

    @MockitoBean
    private ExcluirTipoUsuarioUseCase excluirTipoUsuarioUseCase;

    @MockitoBean
    private BuscarTipoUsuarioUseCase buscarTipoUsuarioUseCase;

    @MockitoBean
    private ListarTipoUsuarioUseCase listarTipoUsuarioUseCase;

    @MockitoBean
    private AssociarUsuarioUseCase associarUsuarioUseCase;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    @Test
    void criar_deveRetornar201_quandoNomeValido() throws Exception {
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("Administrador");

        given(criarTipoUsuarioUseCase.executar(any()))
                .willReturn(TipoUsuarioResponseDTO.builder().id(1L).nome("Administrador").build());

        mockMvc.perform(post("/api/v1/tipos-usuario")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Administrador"));
    }

    @Test
    void criar_deveRetornar409_quandoNomeJaCadastrado() throws Exception {
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("Administrador");

        given(criarTipoUsuarioUseCase.executar(any())).willThrow(new TipoUsuarioJaCadastradoException("Administrador"));

        mockMvc.perform(post("/api/v1/tipos-usuario")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    void atualizar_deveRetornar200_quandoTipoExiste() throws Exception {
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("Dono de Restaurante");

        given(atualizarTipoUsuarioUseCase.executar(eq(1L), any()))
                .willReturn(TipoUsuarioResponseDTO.builder().id(1L).nome("Dono de Restaurante").build());

        mockMvc.perform(put("/api/v1/tipos-usuario/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Dono de Restaurante"));
    }

    @Test
    void atualizar_deveRetornar404_quandoTipoNaoEncontrado() throws Exception {
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("Cliente");

        given(atualizarTipoUsuarioUseCase.executar(eq(99L), any())).willThrow(new TipoUsuarioNaoEncontradoException(99L));

        mockMvc.perform(put("/api/v1/tipos-usuario/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void excluir_deveRetornar204_quandoTipoExiste() throws Exception {
        mockMvc.perform(delete("/api/v1/tipos-usuario/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void excluir_deveRetornar404_quandoTipoNaoEncontrado() throws Exception {
        org.mockito.BDDMockito.willThrow(new TipoUsuarioNaoEncontradoException(99L))
                .given(excluirTipoUsuarioUseCase).executar(99L);

        mockMvc.perform(delete("/api/v1/tipos-usuario/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorId_deveRetornar200_quandoTipoExiste() throws Exception {
        given(buscarTipoUsuarioUseCase.executar(1L))
                .willReturn(TipoUsuarioResponseDTO.builder().id(1L).nome("Administrador").build());

        mockMvc.perform(get("/api/v1/tipos-usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Administrador"));
    }

    @Test
    void buscarPorId_deveRetornar404_quandoTipoNaoEncontrado() throws Exception {
        given(buscarTipoUsuarioUseCase.executar(99L)).willThrow(new TipoUsuarioNaoEncontradoException(99L));

        mockMvc.perform(get("/api/v1/tipos-usuario/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listar_deveRetornar200ComLista() throws Exception {
        given(listarTipoUsuarioUseCase.executar())
                .willReturn(List.of(TipoUsuarioResponseDTO.builder().id(1L).nome("Administrador").build()));

        mockMvc.perform(get("/api/v1/tipos-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Administrador"));
    }

    @Test
    void associarUsuario_deveRetornar200_quandoAmbosExistem() throws Exception {
        given(associarUsuarioUseCase.executar(1L, 1L))
                .willReturn(UsuarioResponseDTO.builder().id(1L).nome("Sophia Amaral Silva").build());

        mockMvc.perform(patch("/api/v1/tipos-usuario/1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Sophia Amaral Silva"));
    }

    @Test
    void associarUsuario_deveRetornar404_quandoUsuarioNaoEncontrado() throws Exception {
        given(associarUsuarioUseCase.executar(99L, 1L)).willThrow(new UsuarioNaoEncontradoException(99L));

        mockMvc.perform(patch("/api/v1/tipos-usuario/1/usuarios/99"))
                .andExpect(status().isNotFound());
    }
}
