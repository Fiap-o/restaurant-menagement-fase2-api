package com.fiap.restaurant_management.application.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestauranteResponseDTO {

    @Schema(description = "ID do restaurante", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Cantina da Nonna")
    private String nome;

    @Schema(description = "Endereço do restaurante")
    private EnderecoDTO endereco;

    @Schema(description = "Tipo de cozinha", example = "Italiana")
    private String tipoCozinha;

    @Schema(description = "Horário de funcionamento", example = "Seg a Sáb, 11h às 23h")
    private String horarioFuncionamento;

    @Schema(description = "Usuário responsável pelo restaurante")
    private UsuarioResponseDTO dono;
}
