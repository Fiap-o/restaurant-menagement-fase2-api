package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.RestauranteCreateDTO;
import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.domain.model.Restaurante;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestauranteMapperImpl implements RestauranteMapper {

    private final EnderecoMapper enderecoMapper;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Restaurante toEntity(RestauranteCreateDTO dto) {
        Restaurante restaurante = new Restaurante();
        restaurante.setNome(dto.getNome());
        restaurante.setEndereco(enderecoMapper.toEntity(dto.getEndereco()));
        restaurante.setTipoCozinha(dto.getTipoCozinha());
        restaurante.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        return restaurante;
    }

    @Override
    public RestauranteResponseDTO toResponseDTO(Restaurante restaurante) {
        return RestauranteResponseDTO.builder()
                .id(restaurante.getId())
                .nome(restaurante.getNome())
                .endereco(enderecoMapper.toDTO(restaurante.getEndereco()))
                .tipoCozinha(restaurante.getTipoCozinha())
                .horarioFuncionamento(restaurante.getHorarioFuncionamento())
                .dono(usuarioMapper.toResponseDTO(restaurante.getDono()))
                .build();
    }
}
