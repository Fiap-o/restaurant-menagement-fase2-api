package com.fiap.restaurant_management.application.usecase.auth;

import com.fiap.restaurant_management.application.port.TokenService;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import com.fiap.restaurant_management.application.dto.LoginDTO;
import com.fiap.restaurant_management.application.dto.TokenResponseDTO;
import com.fiap.restaurant_management.domain.exception.CredenciaisInvalidasException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    public TokenResponseDTO executar(LoginDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getSenha()));
        } catch (BadCredentialsException ex) {
            throw new CredenciaisInvalidasException();
        }

        Usuario usuario = usuarioRepository.findByLogin(dto.getLogin())
                .orElseThrow(CredenciaisInvalidasException::new);

        String token = tokenService.gerarToken(usuario);
        return new TokenResponseDTO(token, "Bearer");
    }
}
