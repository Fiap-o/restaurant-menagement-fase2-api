package com.fiap.restaurant_management.application.usecase.restaurante;

import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExcluirRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;

    public void executar(Long id) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(id));
        restauranteRepository.delete(restaurante);
    }
}
