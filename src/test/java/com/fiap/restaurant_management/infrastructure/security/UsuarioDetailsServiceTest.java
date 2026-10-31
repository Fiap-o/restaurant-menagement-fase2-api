package com.fiap.restaurant_management.infrastructure.security;

import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UsuarioDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioDetailsService usuarioDetailsService;

    @BeforeEach
    void setUp() {
        usuarioDetailsService = new UsuarioDetailsService(usuarioRepository);
    }

    @Test
    void loadUserByUsername_deveRetornarUserDetails_quandoUsuarioExiste() {
        Usuario usuario = Usuario.builder()
                .login("sophia.amaral")
                .senha("senha-criptografada")
                .tipoUsuario(TipoUsuario.builder().nome("Administrador").build())
                .build();

        given(usuarioRepository.findByLogin("sophia.amaral")).willReturn(Optional.of(usuario));

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername("sophia.amaral");

        assertThat(userDetails.getUsername()).isEqualTo("sophia.amaral");
        assertThat(userDetails.getPassword()).isEqualTo("senha-criptografada");
        assertThat(userDetails.getAuthorities())
                .extracting(Object::toString)
                .containsExactly("ROLE_Administrador");
    }

    @Test
    void loadUserByUsername_deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        given(usuarioRepository.findByLogin("inexistente")).willReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioDetailsService.loadUserByUsername("inexistente"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
