package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.TipoUsuarioMapper;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarTipoUsuarioUseCase {

    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final TipoUsuarioMapper tipoUsuarioMapper;

    public TipoUsuarioResponseDTO executar(Long id) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new TipoUsuarioNaoEncontradoException(id));
        return tipoUsuarioMapper.toResponseDTO(tipoUsuario);
    }
}
