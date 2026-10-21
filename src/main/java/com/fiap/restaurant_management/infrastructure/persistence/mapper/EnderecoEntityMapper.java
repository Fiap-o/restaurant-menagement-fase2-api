package com.fiap.restaurant_management.infrastructure.persistence.mapper;

import com.fiap.restaurant_management.domain.model.Endereco;
import com.fiap.restaurant_management.infrastructure.persistence.entity.EnderecoEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class EnderecoEntityMapper {

    public EnderecoEmbeddable toEmbeddable(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoEmbeddable(
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getCep()
        );
    }

    public Endereco toDomain(EnderecoEmbeddable embeddable) {
        if (embeddable == null) {
            return null;
        }
        return new Endereco(
                embeddable.getRua(),
                embeddable.getNumero(),
                embeddable.getCidade(),
                embeddable.getCep()
        );
    }
}
