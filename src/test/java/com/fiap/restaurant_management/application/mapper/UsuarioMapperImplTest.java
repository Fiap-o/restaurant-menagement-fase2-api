package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.application.dto.UsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.domain.model.TipoUsuario;
import com.fiap.restaurant_management.domain.model.Usuario;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioMapperImplTest {

    private final EnderecoMapperImpl enderecoMapper = new EnderecoMapperImpl();
    private final TipoUsuarioMapperImpl tipoUsuarioMapper = new TipoUsuarioMapperImpl();
    private final UsuarioMapperImpl mapper = new UsuarioMapperImpl(enderecoMapper, tipoUsuarioMapper);

    @Test
    void toEntity_deveConverterDtoParaDominio() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO(
                "Sophia Amaral Silva", "sophia.amaral@fiap.com.br", "sophia.amaral", "senhaForte123", 1L,
                new EnderecoDTO("Rua Coronel Joaquim Ferreira de Souza", "541", "São Paulo", "02419-070"));

        Usuario usuario = mapper.toEntity(dto);

        assertThat(usuario.getNome()).isEqualTo("Sophia Amaral Silva");
        assertThat(usuario.getEmail()).isEqualTo("sophia.amaral@fiap.com.br");
        assertThat(usuario.getLogin()).isEqualTo("sophia.amaral");
        assertThat(usuario.getSenha()).isEqualTo("senhaForte123");
        assertThat(usuario.getEndereco().getCidade()).isEqualTo("São Paulo");
    }

    @Test
    void toResponseDTO_deveConverterDominioParaDto() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Sophia Amaral Silva")
                .email("sophia.amaral@fiap.com.br")
                .login("sophia.amaral")
                .tipoUsuario(TipoUsuario.builder().id(1L).nome("Administrador").build())
                .endereco(new com.fiap.restaurant_management.domain.model.Endereco(
                        "Rua Coronel Joaquim Ferreira de Souza", "541", "São Paulo", "02419-070"))
                .dataUltimaAlteracao(LocalDateTime.of(2026, 10, 30, 10, 0))
                .build();

        UsuarioResponseDTO dto = mapper.toResponseDTO(usuario);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNome()).isEqualTo("Sophia Amaral Silva");
        assertThat(dto.getTipoUsuario().getNome()).isEqualTo("Administrador");
        assertThat(dto.getEndereco().getCidade()).isEqualTo("São Paulo");
        assertThat(dto.getDataUltimaAlteracao()).isEqualTo(LocalDateTime.of(2026, 10, 30, 10, 0));
    }
}
