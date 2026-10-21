package com.fiap.restaurant_management.application.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteCreateDTO {

    @Schema(description = "Nome do restaurante", example = "Cantina da Nonna")
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Schema(description = "Endereço do restaurante")
    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoDTO endereco;

    @Schema(description = "Tipo de cozinha", example = "Italiana")
    @NotBlank(message = "Tipo de cozinha é obrigatório")
    private String tipoCozinha;

    @Schema(description = "Horário de funcionamento", example = "Seg a Sáb, 11h às 23h")
    @NotBlank(message = "Horário de funcionamento é obrigatório")
    private String horarioFuncionamento;

    @Schema(description = "ID do usuário responsável pelo restaurante", example = "1")
    @NotNull(message = "Dono do restaurante é obrigatório")
    private Long donoId;
}
