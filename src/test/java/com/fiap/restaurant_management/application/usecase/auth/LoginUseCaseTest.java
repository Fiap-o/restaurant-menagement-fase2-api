package com.fiap.restaurant_management.application.usecase.auth;

import com.fiap.restaurant_management.application.dto.LoginDTO;
import com.fiap.restaurant_management.application.dto.TokenResponseDTO;
import com.fiap.restaurant_management.application.port.AuthenticationPort;
import com.fiap.restaurant_management.application.port.TokenService;
import com.fiap.restaurant_management.domain.exception.CredenciaisInvalidasException;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private AuthenticationPort authenticationPort;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TokenService tokenService;

    private LoginUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new LoginUseCase(authenticationPort, usuarioRepository, tokenService);
    }

    @Test
    void executar_deveRetornarToken_quandoCredenciaisValidas() {
        LoginDTO dto = new LoginDTO("sophia.asilva", "SenhaFort387542");
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setLogin("sophia.asilva");

        given(usuarioRepository.findByLogin("sophia.asilva")).willReturn(Optional.of(usuario));
        given(tokenService.gerarToken(usuario)).willReturn("token-gerado");

        TokenResponseDTO resultado = useCase.executar(dto);

        assertThat(resultado.getToken()).isEqualTo("token-gerado");
        assertThat(resultado.getTipo()).isEqualTo("Bearer");
    }

    @Test
    void executar_devePropagarExcecao_quandoAutenticacaoFalha() {
        LoginDTO dto = new LoginDTO("sophia.asilva", "senhaErrada");

        willThrow(new CredenciaisInvalidasException())
                .given(authenticationPort).autenticar("sophia.asilva", "senhaErrada");

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }

    @Test
    void executar_deveLancarExcecao_quandoUsuarioNaoEncontradoAposAutenticar() {
        LoginDTO dto = new LoginDTO("sophia.asilva", "SenhaFort387542");
        given(usuarioRepository.findByLogin("sophia.asilva")).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(CredenciaisInvalidasException.class);
    }
}
