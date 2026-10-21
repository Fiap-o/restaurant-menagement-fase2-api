package com.fiap.restaurant_management.application.usecase.restaurante;

import com.fiap.restaurant_management.application.dto.RestauranteCreateDTO;
import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.application.mapper.RestauranteMapper;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CriarRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final RestauranteMapper restauranteMapper;

    public RestauranteResponseDTO executar(RestauranteCreateDTO dto) {
        Usuario dono = usuarioRepository.findById(dto.getDonoId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(dto.getDonoId()));

        Restaurante restaurante = restauranteMapper.toEntity(dto);
        restaurante.setDono(dono);

        Restaurante salvo = restauranteRepository.save(restaurante);
        return restauranteMapper.toResponseDTO(salvo);
    }
}
