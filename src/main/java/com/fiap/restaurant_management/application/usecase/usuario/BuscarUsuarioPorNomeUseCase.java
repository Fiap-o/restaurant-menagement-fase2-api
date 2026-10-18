package com.fiap.restaurant_management.application.usecase.usuario;

import com.fiap.restaurant_management.application.mapper.UsuarioMapper;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarUsuarioPorNomeUseCase {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public List<UsuarioResponseDTO> executar(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }
}
