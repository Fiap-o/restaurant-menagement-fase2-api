package com.fiap.restaurant_management.application.usecase.usuario;

import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.application.dto.UsuarioUpdateDTO;
import com.fiap.restaurant_management.application.mapper.EnderecoMapper;
import com.fiap.restaurant_management.application.mapper.UsuarioMapper;
import com.fiap.restaurant_management.domain.exception.EmailJaCadastradoException;
import com.fiap.restaurant_management.domain.exception.LoginJaCadastradoException;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Endereco;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AtualizarUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private EnderecoMapper enderecoMapper;

    private AtualizarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarUsuarioUseCase(usuarioRepository, usuarioMapper, enderecoMapper);
    }

    private Usuario usuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Sophia Amaral Silva");
        usuario.setEmail("sophia.asilva@email.com");
        usuario.setLogin("sophia.asilva");
        return usuario;
    }

    @Test
    void executar_deveAtualizarComSucesso() {
        Usuario existente = usuarioExistente();
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(
                "Sophia Amaral Silva",
                existente.getEmail(),
                existente.getLogin(),
                new EnderecoDTO("Rua Coronel Joaquim Ferreira de Souza", "541", "São Paulo", "02419-070"));

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(enderecoMapper.toEntity(dto.getEndereco()))
                .willReturn(new Endereco("Rua Coronel Joaquim Ferreira de Souza", "541", "São Paulo", "02419-070"));
        given(usuarioRepository.save(any(Usuario.class))).willAnswer(invocation -> invocation.getArgument(0));
        given(usuarioMapper.toResponseDTO(any(Usuario.class)))
                .willReturn(UsuarioResponseDTO.builder().id(1L).nome("Sophia Amaral Silva").build());

        UsuarioResponseDTO resultado = useCase.executar(1L, dto);

        assertThat(resultado.getNome()).isEqualTo("Sophia Amaral Silva");
        verify(usuarioRepository).save(existente);
    }

    @Test
    void executar_deveLancarExcecao_quandoUsuarioNaoEncontrado() {
        given(usuarioRepository.findById(99L)).willReturn(Optional.empty());
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO("Nome", "email@email.com", "login",
                new EnderecoDTO("Rua", "1", "Cidade", "00000-000"));

        assertThatThrownBy(() -> useCase.executar(99L, dto))
                .isInstanceOf(UsuarioNaoEncontradoException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoNovoEmailJaUsadoPorOutroUsuario() {
        Usuario existente = usuarioExistente();
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(
                existente.getNome(), "outro.email@email.com", existente.getLogin(),
                new EnderecoDTO("Rua", "1", "Cidade", "00000-000"));

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(usuarioRepository.existsByEmail("outro.email@email.com")).willReturn(true);

        assertThatThrownBy(() -> useCase.executar(1L, dto))
                .isInstanceOf(EmailJaCadastradoException.class);

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void executar_deveLancarExcecao_quandoNovoLoginJaUsadoPorOutroUsuario() {
        Usuario existente = usuarioExistente();
        UsuarioUpdateDTO dto = new UsuarioUpdateDTO(
                existente.getNome(), existente.getEmail(), "outrologin",
                new EnderecoDTO("Rua", "1", "Cidade", "00000-000"));

        given(usuarioRepository.findById(1L)).willReturn(Optional.of(existente));
        given(usuarioRepository.findByLogin("outrologin")).willReturn(Optional.of(new Usuario()));

        assertThatThrownBy(() -> useCase.executar(1L, dto))
                .isInstanceOf(LoginJaCadastradoException.class);

        verify(usuarioRepository, never()).save(any());
    }
}
