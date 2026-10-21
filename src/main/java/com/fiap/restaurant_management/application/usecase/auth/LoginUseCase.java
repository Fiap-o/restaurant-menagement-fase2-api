package com.fiap.restaurant_management.application.usecase.auth;

import com.fiap.restaurant_management.application.port.AuthenticationPort;
import com.fiap.restaurant_management.application.port.TokenService;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import com.fiap.restaurant_management.application.dto.LoginDTO;
import com.fiap.restaurant_management.application.dto.TokenResponseDTO;
import com.fiap.restaurant_management.domain.exception.CredenciaisInvalidasException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final AuthenticationPort authenticationPort;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    public TokenResponseDTO executar(LoginDTO dto) {
        authenticationPort.autenticar(dto.getLogin(), dto.getSenha());

        Usuario usuario = usuarioRepository.findByLogin(dto.getLogin())
                .orElseThrow(CredenciaisInvalidasException::new);

        String token = tokenService.gerarToken(usuario);
        return new TokenResponseDTO(token, "Bearer");
    }
}
