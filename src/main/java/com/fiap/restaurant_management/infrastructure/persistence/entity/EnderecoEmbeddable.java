package com.fiap.restaurant_management.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoEmbeddable {
    private String rua;
    private String numero;
    private String cidade;
    private String cep;
}
