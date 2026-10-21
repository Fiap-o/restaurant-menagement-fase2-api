package com.fiap.restaurant_management.application.usecase.tipousuario;

import com.fiap.restaurant_management.domain.exception.TipoUsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.repository.TipoUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExcluirTipoUsuarioUseCase {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    public void executar(Long id) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new TipoUsuarioNaoEncontradoException(id));
        tipoUsuarioRepository.delete(tipoUsuario);
    }
}
