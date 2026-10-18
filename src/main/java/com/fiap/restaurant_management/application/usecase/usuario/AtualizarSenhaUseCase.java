package com.fiap.restaurant_management.application.usecase.usuario;

import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import com.fiap.restaurant_management.application.dto.SenhaUpdateDTO;
import com.fiap.restaurant_management.domain.exception.SenhaAtualInvalidaException;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AtualizarSenhaUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void executar(Long id, SenhaUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenha())) {
            throw new SenhaAtualInvalidaException();
        }

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuario.setDataUltimaAlteracao(LocalDateTime.now());
        usuarioRepository.save(usuario);
    }
}
