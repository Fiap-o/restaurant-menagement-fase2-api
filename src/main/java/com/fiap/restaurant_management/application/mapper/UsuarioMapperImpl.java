package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.UsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.domain.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioMapperImpl implements UsuarioMapper {

    private final EnderecoMapper enderecoMapper;
    private final TipoUsuarioMapper tipoUsuarioMapper;

    @Override
    public Usuario toEntity(UsuarioCreateDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setLogin(dto.getLogin());
        usuario.setSenha(dto.getSenha());
        usuario.setEndereco(enderecoMapper.toEntity(dto.getEndereco()));
        return usuario;
    }

    @Override
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .login(usuario.getLogin())
                .tipoUsuario(tipoUsuarioMapper.toResponseDTO(usuario.getTipoUsuario()))
                .endereco(enderecoMapper.toDTO(usuario.getEndereco()))
                .dataUltimaAlteracao(usuario.getDataUltimaAlteracao())
                .build();
    }
}
