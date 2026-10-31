package com.fiap.restaurant_management.application.usecase.usuario;

import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.application.dto.UsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.UsuarioMapper;
import com.fiap.restaurant_management.domain.exception.EmailJaCadastradoException;
import com.fiap.restaurant_management.domain.exception.LoginJaCadastradoException;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CriarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CriarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CriarUsuarioUseCase(usuarioRepository, tipoUsuarioRepository, usuarioMapper, passwordEncoder);
    }

    private UsuarioCreateDTO dtoValido() {
        return new UsuarioCreateDTO(
                "Erick Roberto Ribeiro",
                "erick.ribeiro@email.com",
                "erickribeiro",
                "SenhaFort387542",
                2L,
                new EnderecoDTO("Rua Tiradentes", "1957", "São Bernardo do Campo", "09985-265"));
    }

    private TipoUsuario tipoUsuarioExistente() {
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(2L);
        tipoUsuario.setNome("DONO_RESTAURANTE");
        return tipoUsuario;
    }

    @Test
    void executar_deveCriarUsuarioComSucesso_quandoDadosValidos() {
        UsuarioCreateDTO dto = dtoValido();
        Usuario usuarioMapeado = new Usuario();
        usuarioMapeado.setNome(dto.getNome());
        usuarioMapeado.setEmail(dto.getEmail());
        usuarioMapeado.setLogin(dto.getLogin());

        given(usuarioRepository.existsByEmail(dto.getEmail())).willReturn(false);
        given(usuarioRepository.findByLogin(dto.getLogin())).willReturn(Optional.empty());
        given(tipoUsuarioRepository.findById(2L)).willReturn(Optional.of(tipoUsuarioExistente()));
        given(usuarioMapper.toEntity(dto)).willReturn(usuarioMapeado);
        given(passwordEncoder.encode(dto.getSenha())).willReturn("senha-hash");
        given(usuarioRepository.save(any(Usuario.class))).willAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            return usuario;
        });
        given(usuarioMapper.toResponseDTO(any(Usuario.class))).willReturn(
                UsuarioResponseDTO.builder().id(1L).nome(dto.getNome()).build());

        UsuarioResponseDTO resultado = useCase.executar(dto);

        assertThat(resultado.getId()).isEqualTo(1L);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getSenha()).isEqualTo("senha-hash");
        assertThat(captor.getValue().getTipoUsuario().getId()).isEqualTo(2L);
        assertThat(captor.getValue().getDataUltimaAlteracao()).isNotNull();
    }

    @Test
    void executar_deveLancarExcecao_quandoEmailJaCadastrado() {
        UsuarioCreateDTO dto = dtoValido();
        given(usuarioRepository.existsByEmail(dto.getEmail())).willReturn(true);

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(EmailJaCadastradoException.class)
                .hasMessageContaining(dto.getEmail());

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoLoginJaCadastrado() {
        UsuarioCreateDTO dto = dtoValido();
        given(usuarioRepository.existsByEmail(dto.getEmail())).willReturn(false);
        given(usuarioRepository.findByLogin(dto.getLogin())).willReturn(Optional.of(new Usuario()));

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(LoginJaCadastradoException.class)
                .hasMessageContaining(dto.getLogin());

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoTipoUsuarioNaoEncontrado() {
        UsuarioCreateDTO dto = dtoValido();
        given(usuarioRepository.existsByEmail(dto.getEmail())).willReturn(false);
        given(usuarioRepository.findByLogin(dto.getLogin())).willReturn(Optional.empty());
        given(tipoUsuarioRepository.findById(2L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.executar(dto))
                .isInstanceOf(TipoUsuarioNaoEncontradoException.class);

        verify(usuarioRepository, never()).save(any());
    }
}
