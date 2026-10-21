package com.fiap.restaurant_management.application.usecase.usuario;

import com.fiap.restaurant_management.application.mapper.UsuarioMapper;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import com.fiap.restaurant_management.application.dto.UsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.domain.exception.EmailJaCadastradoException;
import com.fiap.restaurant_management.domain.exception.LoginJaCadastradoException;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CriarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO executar(UsuarioCreateDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new EmailJaCadastradoException(dto.getEmail());
        }
        if (usuarioRepository.findByLogin(dto.getLogin()).isPresent()) {
            throw new LoginJaCadastradoException(dto.getLogin());
        }

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(dto.getTipoUsuarioId())
                .orElseThrow(() -> new TipoUsuarioNaoEncontradoException(dto.getTipoUsuarioId()));

        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setDataUltimaAlteracao(LocalDateTime.now());

        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(salvo);
    }
}
