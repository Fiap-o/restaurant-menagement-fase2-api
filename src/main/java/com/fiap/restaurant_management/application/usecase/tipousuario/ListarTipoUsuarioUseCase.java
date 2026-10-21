package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.application.mapper.TipoUsuarioMapper;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarTipoUsuarioUseCase {

    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final TipoUsuarioMapper tipoUsuarioMapper;

    public List<TipoUsuarioResponseDTO> executar() {
        return tipoUsuarioRepository.findAll()
                .stream()
                .map(tipoUsuarioMapper::toResponseDTO)
                .toList();
    }
}
