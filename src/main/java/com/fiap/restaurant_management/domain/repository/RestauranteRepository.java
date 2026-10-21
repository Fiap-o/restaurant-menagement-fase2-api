package com.fiap.restaurant_management.domain.repository;

import com.fiap.restaurant_management.domain.model.Restaurante;

import java.util.List;
import java.util.Optional;

public interface RestauranteRepository {

    Restaurante save(Restaurante restaurante);

    void delete(Restaurante restaurante);

    Optional<Restaurante> findById(Long id);

    List<Restaurante> findAll();
}
