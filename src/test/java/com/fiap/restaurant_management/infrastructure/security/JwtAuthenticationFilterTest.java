package com.fiap.restaurant_management.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private UsuarioDetailsService usuarioDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new JwtAuthenticationFilter(jwtTokenService, usuarioDetailsService);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_deveSeguirCadeia_quandoNaoHaCabecalhoAuthorization() throws Exception {
        given(request.getHeader("Authorization")).willReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternal_deveAutenticar_quandoTokenValido() throws Exception {
        UserDetails userDetails = User.builder()
                .username("sophia.amaral")
                .password("senha-criptografada")
                .authorities(List.of())
                .build();

        given(request.getHeader("Authorization")).willReturn("Bearer token-valido");
        given(jwtTokenService.extractUsername("token-valido")).willReturn("sophia.amaral");
        given(usuarioDetailsService.loadUserByUsername("sophia.amaral")).willReturn(userDetails);
        given(jwtTokenService.isTokenValido("token-valido", userDetails)).willReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("sophia.amaral");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_naoDeveAutenticar_quandoTokenInvalido() throws Exception {
        UserDetails userDetails = User.builder()
                .username("erick.ribeiro")
                .password("senha-criptografada")
                .authorities(List.of())
                .build();

        given(request.getHeader("Authorization")).willReturn("Bearer token-invalido");
        given(jwtTokenService.extractUsername("token-invalido")).willReturn("erick.ribeiro");
        given(usuarioDetailsService.loadUserByUsername("erick.ribeiro")).willReturn(userDetails);
        given(jwtTokenService.isTokenValido("token-invalido", userDetails)).willReturn(false);

        filter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_deveSeguirCadeia_quandoCabecalhoNaoComecaComBearer() throws Exception {
        given(request.getHeader("Authorization")).willReturn("Basic abc123");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
