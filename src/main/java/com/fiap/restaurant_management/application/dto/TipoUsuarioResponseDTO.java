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
public class TipoUsuarioResponseDTO {

    @Schema(description = "ID do tipo de usuário", example = "1")
    private Long id;

    @Schema(description = "Nome do tipo de usuário", example = "CLIENTE")
    private String nome;
}
