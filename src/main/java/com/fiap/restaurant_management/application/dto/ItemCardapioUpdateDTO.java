package com.fiap.restaurant_management.application.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCardapioUpdateDTO {

    @Schema(description = "Nome do item", example = "Risoto de Funghi")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Descrição do item", example = "Arroz arbóreo, funghi secchi e parmesão")
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @Schema(description = "Preço do item", example = "59.90")
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    @Schema(description = "Se o item só pode ser consumido no local (true) ou também pode ser pedido para entrega/retirada (false)", example = "false")
    private boolean disponivelSomenteNoLocal;

    @Schema(description = "Caminho de onde a foto do prato estaria armazenada (sem upload real)", example = "/fotos/risoto-funghi.jpg")
    private String caminhoFoto;
}
