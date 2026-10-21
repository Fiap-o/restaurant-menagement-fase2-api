package com.fiap.restaurant_management.application.usecase.restaurante;

import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.application.dto.RestauranteUpdateDTO;
import com.fiap.restaurant_management.application.mapper.EnderecoMapper;
import com.fiap.restaurant_management.application.mapper.RestauranteMapper;
import com.fiap.restaurant_management.domain.exception.RestauranteNaoEncontradoException;
import com.fiap.restaurant_management.domain.exception.UsuarioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.model.Usuario;
import com.fiap.restaurant_management.domain.repository.RestauranteRepository;
import com.fiap.restaurant_management.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarRestauranteUseCase {

    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;
    private final RestauranteMapper restauranteMapper;
    private final EnderecoMapper enderecoMapper;

    public RestauranteResponseDTO executar(Long id, RestauranteUpdateDTO dto) {
        Restaurante restaurante = restauranteRepository.findById(id)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(id));

        Usuario dono = usuarioRepository.findById(dto.getDonoId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException(dto.getDonoId()));

        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(enderecoMapper.toEntity(dto.getEndereco()));
        restaurante.setTipoCozinha(dto.getTipoCozinha());
        restaurante.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        restaurante.setDono(dono);

        Restaurante atualizado = restauranteRepository.save(restaurante);
        return restauranteMapper.toResponseDTO(atualizado);
    }
}
