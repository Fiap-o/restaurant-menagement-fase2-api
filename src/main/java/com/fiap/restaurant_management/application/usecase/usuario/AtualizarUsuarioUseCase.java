package com.fiap.restaurant_management.application.usecase.usuario;

import com.fiap.restaurant_management.application.mapper.EnderecoMapper;
import com.fiap.restaurant_management.application.mapper.UsuarioMapper;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.application.dto.UsuarioUpdateDTO;
import com.fiap.restaurant_management.domain.exception.EmailJaCadastradoException;
import com.fiap.restaurant_management.domain.exception.LoginJaCadastradoException;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AtualizarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final EnderecoMapper enderecoMapper;

    public UsuarioResponseDTO executar(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));

        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException(dto.getEmail());
        }
        if (!usuario.getLogin().equals(dto.getLogin()) && usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new LoginJaCadastradoException(dto.getLogin());
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setLogin(dto.getLogin());
        usuario.setEndereco(enderecoMapper.toEntity(dto.getEndereco()));
        usuario.setDataUltimaAlteracao(LocalDateTime.now());

        Usuario atualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(atualizado);
    }
}
