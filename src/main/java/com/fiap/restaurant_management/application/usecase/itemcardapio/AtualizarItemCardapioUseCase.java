package com.fiap.restaurant_management.application.usecase.itemcardapio;

import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.application.dto.ItemCardapioUpdateDTO;
import com.fiap.restaurant_management.application.mapper.ItemCardapioMapper;
import com.fiap.restaurant_management.domain.exception.ItemCardapioNaoEncontradoException;
import com.fiap.restaurant_management.domain.model.ItemCardapio;
import com.fiap.restaurant_management.domain.repository.ItemCardapioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AtualizarItemCardapioUseCase {

    private final ItemCardapioRepository itemCardapioRepository;
    private final ItemCardapioMapper itemCardapioMapper;

    public ItemCardapioResponseDTO executar(Long id, ItemCardapioUpdateDTO dto) {
        ItemCardapio itemCardapio = itemCardapioRepository.findById(id)
                .orElseThrow(() -> new ItemCardapioNaoEncontradoException(id));

        itemCardapio.setNome(dto.getNome());
        itemCardapio.setDescricao(dto.getDescricao());
        itemCardapio.setPreco(dto.getPreco());
        itemCardapio.setDisponivelSomenteNoLocal(dto.isDisponivelSomenteNoLocal());
        itemCardapio.setCaminhoFoto(dto.getCaminhoFoto());

        ItemCardapio atualizado = itemCardapioRepository.save(itemCardapio);
        return itemCardapioMapper.toResponseDTO(atualizado);
    }
}
