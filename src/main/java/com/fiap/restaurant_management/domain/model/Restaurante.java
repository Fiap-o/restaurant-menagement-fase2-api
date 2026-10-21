package com.fiap.restaurant_management.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Restaurante {

    private Long id;

    private String nome;

    private Endereco endereco;

    private String tipoCozinha;

    private String horarioFuncionamento;

    private Usuario dono;
}
