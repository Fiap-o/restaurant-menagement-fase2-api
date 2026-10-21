package com.fiap.restaurant_management.application.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoUsuarioCreateDTO {

    @Schema(description = "Nome do tipo de usuário", example = "CLIENTE")
    @NotBlank(message = "Nome do tipo é obrigatório")
    private String nome;
}
