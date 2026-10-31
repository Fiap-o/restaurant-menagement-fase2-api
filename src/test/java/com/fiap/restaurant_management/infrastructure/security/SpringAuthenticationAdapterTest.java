package com.fiap.restaurant_management.infrastructure.security;

import com.fiap.restaurant_management.domain.exception.CredenciaisInvalidasException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SpringAuthenticationAdapterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    private SpringAuthenticationAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SpringAuthenticationAdapter(authenticationManager);
    }

    @Test
    void autenticar_deveChamarAuthenticationManager_quandoCredenciaisValidas() {
        adapter.autenticar("sophia.amaral", "senhaForte123");

        verify(authenticationManager).authenticate(any());
    }

    @Test
    void autenticar_deveLancarCredenciaisInvalidas_quandoBadCredentials() {
        given(authenticationManager.authenticate(any())).willThrow(new BadCredentialsException("credenciais inválidas"));

        assertThatThrownBy(() -> adapter.autenticar("erick.ribeiro", "senhaErrada"))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }
}
