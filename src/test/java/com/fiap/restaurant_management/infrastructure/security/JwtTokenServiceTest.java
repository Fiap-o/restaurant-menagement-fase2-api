package com.fiap.restaurant_management.infrastructure.security;

import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService = new JwtTokenService();
        ReflectionTestUtils.setField(jwtTokenService, "secret", "chave-secreta-para-o-tech-challenge-troque-em-producao-123456");
        ReflectionTestUtils.setField(jwtTokenService, "expiration", 3600000L);
    }

    private Usuario usuarioValido() {
        return Usuario.builder()
                .id(1L)
                .nome("Sophia Amaral Silva")
                .login("sophia.amaral")
                .tipoUsuario(TipoUsuario.builder().id(1L).nome("Administrador").build())
                .build();
    }

    @Test
    void gerarToken_deveGerarTokenComSubjectIgualAoLogin() {
        String token = jwtTokenService.gerarToken(usuarioValido());

        assertThat(token).isNotBlank();
        assertThat(jwtTokenService.extractUsername(token)).isEqualTo("sophia.amaral");
    }

    @Test
    void isTokenValido_deveRetornarTrue_quandoTokenCorrespondeAoUsuario() {
        String token = jwtTokenService.gerarToken(usuarioValido());
        UserDetails userDetails = User.builder()
                .username("sophia.amaral")
                .password("senha-criptografada")
                .authorities(List.of())
                .build();

        assertThat(jwtTokenService.isTokenValido(token, userDetails)).isTrue();
    }

    @Test
    void isTokenValido_deveRetornarFalse_quandoUsernameDiferente() {
        String token = jwtTokenService.gerarToken(usuarioValido());
        UserDetails userDetails = User.builder()
                .username("erick.ribeiro")
                .password("senha-criptografada")
                .authorities(List.of())
                .build();

        assertThat(jwtTokenService.isTokenValido(token, userDetails)).isFalse();
    }

    @Test
    void gerarToken_deveGerarTokensDiferentes_paraUsuariosDiferentes() {
        Usuario erick = Usuario.builder()
                .id(2L)
                .nome("Erick Roberto Ribeiro")
                .login("erick.ribeiro")
                .tipoUsuario(TipoUsuario.builder().id(1L).nome("Administrador").build())
                .build();

        String tokenSophia = jwtTokenService.gerarToken(usuarioValido());
        String tokenErick = jwtTokenService.gerarToken(erick);

        assertThat(jwtTokenService.extractUsername(tokenSophia)).isEqualTo("sophia.amaral");
        assertThat(jwtTokenService.extractUsername(tokenErick)).isEqualTo("erick.ribeiro");
    }
}
