package com.fiap.restaurant_management.application.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCardapioResponseDTO {

    @Schema(description = "ID do item", example = "1")
    private Long id;

    @Schema(description = "Nome do item", example = "Risoto de Funghi")
    private String nome;

    @Schema(description = "Descrição do item", example = "Arroz arbóreo, funghi secchi e parmesão")
    private String descricao;

    @Schema(description = "Preço do item", example = "59.90")
    private BigDecimal preco;

    @Schema(description = "Se o item só pode ser consumido no local", example = "false")
    private boolean disponivelSomenteNoLocal;

    @Schema(description = "Caminho de onde a foto do prato estaria armazenada", example = "/fotos/risoto-funghi.jpg")
    private String caminhoFoto;

    @Schema(description = "ID do restaurante ao qual o item pertence", example = "1")
    private Long restauranteId;
}
