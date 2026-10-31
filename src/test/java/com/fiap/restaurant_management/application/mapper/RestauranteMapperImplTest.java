package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.EnderecoDTO;
import com.fiap.restaurant_management.application.dto.RestauranteCreateDTO;
import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.domain.model.Restaurante;
import com.fiap.restaurant_management.domain.model.Usuario;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RestauranteMapperImplTest {

    private final EnderecoMapperImpl enderecoMapper = new EnderecoMapperImpl();
    private final TipoUsuarioMapperImpl tipoUsuarioMapper = new TipoUsuarioMapperImpl();
    private final UsuarioMapperImpl usuarioMapper = new UsuarioMapperImpl(enderecoMapper, tipoUsuarioMapper);
    private final RestauranteMapperImpl mapper = new RestauranteMapperImpl(enderecoMapper, usuarioMapper);

    @Test
    void toEntity_deveConverterDtoParaDominio() {
        RestauranteCreateDTO dto = new RestauranteCreateDTO(
                "Cantina da Sophia",
                new EnderecoDTO("Rua Coronel Joaquim Ferreira de Souza", "541", "São Paulo", "02419-070"),
                "Italiana", "11:00-23:00", 1L);

        Restaurante restaurante = mapper.toEntity(dto);

        assertThat(restaurante.getNome()).isEqualTo("Cantina da Sophia");
        assertThat(restaurante.getTipoCozinha()).isEqualTo("Italiana");
        assertThat(restaurante.getHorarioFuncionamento()).isEqualTo("11:00-23:00");
        assertThat(restaurante.getEndereco().getCidade()).isEqualTo("São Paulo");
    }

    @Test
    void toResponseDTO_deveConverterDominioParaDto_comDono() {
        Usuario dono = Usuario.builder().id(1L).nome("Sophia Amaral Silva").build();
        Restaurante restaurante = Restaurante.builder()
                .id(1L)
                .nome("Cantina da Sophia")
                .tipoCozinha("Italiana")
                .horarioFuncionamento("11:00-23:00")
                .dono(dono)
                .build();

        RestauranteResponseDTO dto = mapper.toResponseDTO(restaurante);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNome()).isEqualTo("Cantina da Sophia");
        assertThat(dto.getDono().getNome()).isEqualTo("Sophia Amaral Silva");
    }
}
