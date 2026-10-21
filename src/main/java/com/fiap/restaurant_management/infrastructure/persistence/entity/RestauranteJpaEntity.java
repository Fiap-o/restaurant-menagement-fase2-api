package com.fiap.restaurant_management.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurante")
@EqualsAndHashCode(of = "id")
public class RestauranteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Embedded
    private EnderecoEmbeddable endereco;

    @Column(name = "tipo_cozinha", nullable = false)
    private String tipoCozinha;

    @Column(name = "horario_funcionamento", nullable = false)
    private String horarioFuncionamento;

    @ManyToOne
    @JoinColumn(name = "dono_id", nullable = false)
    private UsuarioJpaEntity dono;
}
