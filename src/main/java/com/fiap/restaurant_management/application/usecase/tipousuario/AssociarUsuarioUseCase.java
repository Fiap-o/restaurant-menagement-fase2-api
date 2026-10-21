package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.UsuarioMapper;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssociarUsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioResponseDTO executar(Long usuarioId, Long tipoUsuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(tipoUsuarioId)
                .orElseThrow(() -> new TipoUsuarioNaoEncontradoException(tipoUsuarioId));

        usuario.setTipoUsuario(tipoUsuario);
        Usuario atualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(atualizado);
    }
}
