package com.fiap.restaurant_management.infrastructure.security;

import com.fiap.restaurant_management.application.port.AuthenticationPort;
import com.fiap.restaurant_management.domain.exception.CredenciaisInvalidasException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringAuthenticationAdapter implements AuthenticationPort {

    private final AuthenticationManager authenticationManager;

    @Override
    public void autenticar(String login, String senha) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, senha));
        } catch (BadCredentialsException ex) {
            throw new CredenciaisInvalidasException();
        }
    }
}
