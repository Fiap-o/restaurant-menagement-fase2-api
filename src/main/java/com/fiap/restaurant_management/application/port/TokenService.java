package com.fiap.restaurant_management.application.port;

import com.fiap.restaurant_management.domain.model.Usuario;

public interface TokenService {

    String gerarToken(Usuario usuario);
}
