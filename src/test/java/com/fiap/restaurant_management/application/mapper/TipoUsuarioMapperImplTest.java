package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.TipoUsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TipoUsuarioMapperImplTest {

    private final TipoUsuarioMapperImpl mapper = new TipoUsuarioMapperImpl();

    @Test
    void toEntity_deveConverterDtoParaDominio() {
        TipoUsuarioCreateDTO dto = new TipoUsuarioCreateDTO("Administrador");

        TipoUsuario tipoUsuario = mapper.toEntity(dto);

        assertThat(tipoUsuario.getNome()).isEqualTo("Administrador");
    }

    @Test
    void toEntity_deveRetornarNull_quandoDtoNull() {
        assertThat(mapper.toEntity(null)).isNull();
    }

    @Test
    void toResponseDTO_deveConverterDominioParaDto() {
        TipoUsuario tipoUsuario = TipoUsuario.builder().id(1L).nome("Administrador").build();

        TipoUsuarioResponseDTO dto = mapper.toResponseDTO(tipoUsuario);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNome()).isEqualTo("Administrador");
    }

    @Test
    void toResponseDTO_deveRetornarNull_quandoTipoUsuarioNull() {
        assertThat(mapper.toResponseDTO(null)).isNull();
    }
}
