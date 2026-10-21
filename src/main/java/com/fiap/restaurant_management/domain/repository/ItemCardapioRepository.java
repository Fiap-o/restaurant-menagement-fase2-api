package com.fiap.restaurant_management.domain.repository;

import com.fiap.restaurant_management.domain.model.ItemCardapio;

import java.util.List;
import java.util.Optional;

public interface ItemCardapioRepository {

    ItemCardapio save(ItemCardapio itemCardapio);

    void delete(ItemCardapio itemCardapio);

    Optional<ItemCardapio> findById(Long id);

    List<ItemCardapio> findByRestauranteId(Long restauranteId);
}
