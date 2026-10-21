package com.fiap.restaurant_management.infrastructure.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import com.fiap.restaurant_management.application.dto.RestauranteCreateDTO;
import com.fiap.restaurant_management.application.dto.RestauranteResponseDTO;
import com.fiap.restaurant_management.application.dto.RestauranteUpdateDTO;
import com.fiap.restaurant_management.application.usecase.restaurante.AtualizarRestauranteUseCase;
import com.fiap.restaurant_management.application.usecase.restaurante.BuscarRestaurantePorIdUseCase;
import com.fiap.restaurant_management.application.usecase.restaurante.CriarRestauranteUseCase;
import com.fiap.restaurant_management.application.usecase.restaurante.ExcluirRestauranteUseCase;
import com.fiap.restaurant_management.application.usecase.restaurante.ListarRestauranteUseCase;
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

@Tag(name = "Restaurantes", description = "Cadastro, atualização, consulta e exclusão de restaurantes")
@RestController
@RequestMapping("/api/v1/restaurantes")
public class RestauranteController {

    private final CriarRestauranteUseCase criarRestauranteUseCase;
    private final AtualizarRestauranteUseCase atualizarRestauranteUseCase;
    private final ExcluirRestauranteUseCase excluirRestauranteUseCase;
    private final BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase;
    private final ListarRestauranteUseCase listarRestauranteUseCase;

    public RestauranteController(
            CriarRestauranteUseCase criarRestauranteUseCase,
            AtualizarRestauranteUseCase atualizarRestauranteUseCase,
            ExcluirRestauranteUseCase excluirRestauranteUseCase,
            BuscarRestaurantePorIdUseCase buscarRestaurantePorIdUseCase,
            ListarRestauranteUseCase listarRestauranteUseCase) {
        this.criarRestauranteUseCase = criarRestauranteUseCase;
        this.atualizarRestauranteUseCase = atualizarRestauranteUseCase;
        this.excluirRestauranteUseCase = excluirRestauranteUseCase;
        this.buscarRestaurantePorIdUseCase = buscarRestaurantePorIdUseCase;
        this.listarRestauranteUseCase = listarRestauranteUseCase;
    }

    @Operation(summary = "Cadastrar restaurante", description = "Cria um novo restaurante associado a um usuário dono.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RestauranteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Usuário informado como dono não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<RestauranteResponseDTO> criar(@Valid @RequestBody RestauranteCreateDTO dto) {
        RestauranteResponseDTO criado = criarRestauranteUseCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RestauranteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante ou usuário dono não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> atualizar(
            @Parameter(description = "ID do restaurante") @PathVariable Long id,
            @Valid @RequestBody RestauranteUpdateDTO dto) {
        RestauranteResponseDTO atualizado = atualizarRestauranteUseCase.executar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Excluir restaurante", description = "Remove um restaurante existente pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@Parameter(description = "ID do restaurante") @PathVariable Long id) {
        excluirRestauranteUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os dados de um restaurante a partir do seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RestauranteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponseDTO> buscarPorId(@Parameter(description = "ID do restaurante") @PathVariable Long id) {
        return ResponseEntity.ok(buscarRestaurantePorIdUseCase.executar(id));
    }

    @Operation(summary = "Listar restaurantes", description = "Retorna todos os restaurantes cadastrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RestauranteResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<RestauranteResponseDTO>> listar() {
        return ResponseEntity.ok(listarRestauranteUseCase.executar());
    }
}
