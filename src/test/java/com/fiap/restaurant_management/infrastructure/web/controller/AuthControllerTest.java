package com.fiap.restaurant_management.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.restaurant_management.application.dto.LoginDTO;
import com.fiap.restaurant_management.application.dto.TokenResponseDTO;
import com.fiap.restaurant_management.application.usecase.auth.LoginUseCase;
import com.fiap.restaurant_management.domain.exception.CredenciaisInvalidasException;
import com.fiap.restaurant_management.infrastructure.security.JwtTokenService;
import com.fiap.restaurant_management.infrastructure.security.UsuarioDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private LoginUseCase loginUseCase;

    @MockitoBean
    private JwtTokenService jwtTokenService;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    @Test
    void login_deveRetornar200ComToken_quandoCredenciaisValidas() throws Exception {
        LoginDTO dto = new LoginDTO("sophia.amaral", "senhaForte123");
        given(loginUseCase.executar(any())).willReturn(new TokenResponseDTO("token-jwt-gerado", "Bearer"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-jwt-gerado"))
                .andExpect(jsonPath("$.tipo").value("Bearer"));
    }

    @Test
    void login_deveRetornar401_quandoCredenciaisInvalidas() throws Exception {
        LoginDTO dto = new LoginDTO("erick.ribeiro", "senhaErrada");
        given(loginUseCase.executar(any())).willThrow(new CredenciaisInvalidasException());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_deveRetornar400_quandoLoginEmBranco() throws Exception {
        LoginDTO dto = new LoginDTO("", "senhaForte123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
