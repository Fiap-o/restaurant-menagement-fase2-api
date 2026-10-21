package com.fiap.restaurant_management.infrastructure.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import com.fiap.restaurant_management.application.dto.ItemCardapioCreateDTO;
import com.fiap.restaurant_management.application.dto.ItemCardapioResponseDTO;
import com.fiap.restaurant_management.application.dto.ItemCardapioUpdateDTO;
import com.fiap.restaurant_management.application.usecase.itemcardapio.AtualizarItemCardapioUseCase;
import com.fiap.restaurant_management.application.usecase.itemcardapio.BuscarItemCardapioPorIdUseCase;
import com.fiap.restaurant_management.application.usecase.itemcardapio.CriarItemCardapioUseCase;
import com.fiap.restaurant_management.application.usecase.itemcardapio.ExcluirItemCardapioUseCase;
import com.fiap.restaurant_management.application.usecase.itemcardapio.ListarItemCardapioPorRestauranteUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;

@Tag(name = "Itens de Cardápio", description = "Cadastro, atualização, consulta e exclusão de itens do cardápio dos restaurantes")
@RestController
@RequestMapping("/api/v1/itens-cardapio")
public class ItemCardapioController {

    private final CriarItemCardapioUseCase criarItemCardapioUseCase;
    private final AtualizarItemCardapioUseCase atualizarItemCardapioUseCase;
    private final ExcluirItemCardapioUseCase excluirItemCardapioUseCase;
    private final BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase;
    private final ListarItemCardapioPorRestauranteUseCase listarItemCardapioPorRestauranteUseCase;

    public ItemCardapioController(
            CriarItemCardapioUseCase criarItemCardapioUseCase,
            AtualizarItemCardapioUseCase atualizarItemCardapioUseCase,
            ExcluirItemCardapioUseCase excluirItemCardapioUseCase,
            BuscarItemCardapioPorIdUseCase buscarItemCardapioPorIdUseCase,
            ListarItemCardapioPorRestauranteUseCase listarItemCardapioPorRestauranteUseCase) {
        this.criarItemCardapioUseCase = criarItemCardapioUseCase;
        this.atualizarItemCardapioUseCase = atualizarItemCardapioUseCase;
        this.excluirItemCardapioUseCase = excluirItemCardapioUseCase;
        this.buscarItemCardapioPorIdUseCase = buscarItemCardapioPorIdUseCase;
        this.listarItemCardapioPorRestauranteUseCase = listarItemCardapioPorRestauranteUseCase;
    }

    @Operation(summary = "Cadastrar item de cardápio", description = "Cria um novo item de cardápio associado a um restaurante.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ItemCardapioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante informado não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<ItemCardapioResponseDTO> criar(@Valid @RequestBody ItemCardapioCreateDTO dto) {
        ItemCardapioResponseDTO criado = criarItemCardapioUseCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Atualizar item de cardápio", description = "Atualiza os dados de um item de cardápio existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ItemCardapioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ItemCardapioResponseDTO> atualizar(
            @Parameter(description = "ID do item de cardápio") @PathVariable Long id,
            @Valid @RequestBody ItemCardapioUpdateDTO dto) {
        ItemCardapioResponseDTO atualizado = atualizarItemCardapioUseCase.executar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Excluir item de cardápio", description = "Remove um item de cardápio existente pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@Parameter(description = "ID do item de cardápio") @PathVariable Long id) {
        excluirItemCardapioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar item de cardápio por ID", description = "Retorna os dados de um item de cardápio a partir do seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ItemCardapioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Item não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItemCardapioResponseDTO> buscarPorId(@Parameter(description = "ID do item de cardápio") @PathVariable Long id) {
        return ResponseEntity.ok(buscarItemCardapioPorIdUseCase.executar(id));
    }

    @Operation(summary = "Listar itens de cardápio de um restaurante", description = "Retorna todos os itens de cardápio cadastrados para um restaurante específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ItemCardapioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<List<ItemCardapioResponseDTO>> listarPorRestaurante(
            @Parameter(description = "ID do restaurante") @PathVariable Long restauranteId) {
        return ResponseEntity.ok(listarItemCardapioPorRestauranteUseCase.executar(restauranteId));
    }
}
