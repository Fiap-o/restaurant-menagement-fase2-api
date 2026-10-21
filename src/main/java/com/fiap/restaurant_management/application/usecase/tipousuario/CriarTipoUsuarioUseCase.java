package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.application.dto.TipoUsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.TipoUsuarioMapper;
import com.fiap.restaurant_management.domain.exception.TipoUsuarioJaCadastradoException;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarTipoUsuarioUseCase {

    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final TipoUsuarioMapper tipoUsuarioMapper;

    public TipoUsuarioResponseDTO executar(TipoUsuarioCreateDTO dto) {
        if (tipoUsuarioRepository.existsByNome(dto.getNome())) {
            throw new TipoUsuarioJaCadastradoException(dto.getNome());
        }

        TipoUsuario tipoUsuario = tipoUsuarioMapper.toEntity(dto);
        TipoUsuario salvo = tipoUsuarioRepository.save(tipoUsuario);
        return tipoUsuarioMapper.toResponseDTO(salvo);
    }
}
