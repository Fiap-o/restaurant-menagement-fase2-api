package com.fiap.restaurant_management.application.mapper;

import com.fiap.restaurant_management.application.dto.ItemCardapioCreateDTO;
import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.domain.model.ItemCardapio;
import org.springframework.stereotype.Component;

@Component
public class ItemCardapioMapperImpl implements ItemCardapioMapper {

    @Override
    public ItemCardapio toEntity(ItemCardapioCreateDTO dto) {
        ItemCardapio itemCardapio = new ItemCardapio();
        itemCardapio.setNome(dto.getNome());
        itemCardapio.setDescricao(dto.getDescricao());
        itemCardapio.setPreco(dto.getPreco());
        itemCardapio.setDisponivelSomenteNoLocal(dto.isDisponivelSomenteNoLocal());
        itemCardapio.setCaminhoFoto(dto.getCaminhoFoto());
        return itemCardapio;
    }

    @Override
    public ItemCardapioResponseDTO toResponseDTO(ItemCardapio itemCardapio) {
        return ItemCardapioResponseDTO.builder()
                .id(itemCardapio.getId())
                .nome(itemCardapio.getNome())
                .descricao(itemCardapio.getDescricao())
                .preco(itemCardapio.getPreco())
                .disponivelSomenteNoLocal(itemCardapio.isDisponivelSomenteNoLocal())
                .caminhoFoto(itemCardapio.getCaminhoFoto())
                .restauranteId(itemCardapio.getRestaurante() != null ? itemCardapio.getRestaurante().getId() : null)
                .build();
    }
}
