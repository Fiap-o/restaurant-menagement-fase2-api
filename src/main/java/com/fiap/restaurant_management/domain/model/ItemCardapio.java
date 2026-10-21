package com.fiap.restaurant_management.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ItemCardapio {

    private Long id;

    private String nome;

    private String descricao;

    private BigDecimal preco;

    private boolean disponivelSomenteNoLocal;

    private String caminhoFoto;

    private Restaurante restaurante;
}
