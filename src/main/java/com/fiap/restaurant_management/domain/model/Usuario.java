package com.fiap.restaurant_management.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    private Long id;

    private String nome;

    private String email;

    private String login;

    private String senha;

    private TipoUsuario tipoUsuario;

    private Endereco endereco;

    private LocalDateTime dataUltimaAlteracao;
}
