package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.domain.model.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapperImpl implements EnderecoMapper {

    @Override
    public Endereco toEntity(EnderecoDTO dto) {
        if (dto == null) {
            return null;
        }
        Endereco endereco = new Endereco();
        endereco.setRua(dto.getRua());
        endereco.setNumero(dto.getNumero());
        endereco.setCidade(dto.getCidade());
        endereco.setCep(dto.getCep());
        return endereco;
    }

    @Override
    public EnderecoDTO toDTO(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoDTO(
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getCep()
        );
    }
}
