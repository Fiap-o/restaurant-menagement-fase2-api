package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.domain.model.Endereco;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnderecoMapperImplTest {

    private final EnderecoMapperImpl mapper = new EnderecoMapperImpl();

    @Test
    void toEntity_deveConverterDtoParaDominio() {
        EnderecoDTO dto = new EnderecoDTO("Rua Coronel Joaquim Ferreira de Souza", "541", "São Paulo", "02419-070");

        Endereco endereco = mapper.toEntity(dto);

        assertThat(endereco.getRua()).isEqualTo("Rua Coronel Joaquim Ferreira de Souza");
        assertThat(endereco.getNumero()).isEqualTo("541");
        assertThat(endereco.getCidade()).isEqualTo("São Paulo");
        assertThat(endereco.getCep()).isEqualTo("02419-070");
    }

    @Test
    void toEntity_deveRetornarNull_quandoDtoNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void toDTO_deveConverterDominioParaDto() {
        Endereco endereco = new Endereco("Rua Coronel Joaquim Ferreira de Souza", "541", "São Paulo", "02419-070");

        EnderecoDTO dto = mapper.toDTO(endereco);

        assertThat(dto.getRua()).isEqualTo("Rua Coronel Joaquim Ferreira de Souza");
        assertThat(dto.getNumero()).isEqualTo("541");
        assertThat(dto.getCidade()).isEqualTo("São Paulo");
        assertThat(dto.getCep()).isEqualTo("02419-070");
    }

    @Test
    void toDTO_deveRetornarNull_quandoEnderecoNull() {
        assertThat(mapper.toDTO(null)).isNull();
    }
}
