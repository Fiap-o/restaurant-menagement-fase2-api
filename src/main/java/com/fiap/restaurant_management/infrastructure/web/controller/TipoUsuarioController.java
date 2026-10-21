package com.fiap.restaurant_management.infrastructure.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import com.fiap.restaurant_management.application.dto.TipoUsuarioCreateDTO;
import com.fiap.restaurant_management.application.dto.TipoUsuarioResponseDTO;
import com.fiap.restaurant_management.application.dto.UsuarioResponseDTO;
import com.fiap.restaurant_management.application.usecase.tipousuario.AssociarUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.tipousuario.AtualizarTipoUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.tipousuario.BuscarTipoUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.tipousuario.CriarTipoUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.tipousuario.ExcluirTipoUsuarioUseCase;
import com.fiap.restaurant_management.application.usecase.tipousuario.ListarTipoUsuarioUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
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

@Tag(name = "Tipos de Usuário", description = "Cadastro de tipos de usuário e associação a usuários existentes")
@RestController
@RequestMapping("/api/v1/tipos-usuario")
public class TipoUsuarioController {

    private final CriarTipoUsuarioUseCase criarTipoUsuarioUseCase;
    private final AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase;
    private final ExcluirTipoUsuarioUseCase excluirTipoUsuarioUseCase;
    private final BuscarTipoUsuarioUseCase buscarTipoUsuarioUseCase;
    private final ListarTipoUsuarioUseCase listarTipoUsuarioUseCase;
    private final AssociarUsuarioUseCase associarUsuarioUseCase;

    public TipoUsuarioController(
            CriarTipoUsuarioUseCase criarTipoUsuarioUseCase,
            AtualizarTipoUsuarioUseCase atualizarTipoUsuarioUseCase,
            ExcluirTipoUsuarioUseCase excluirTipoUsuarioUseCase,
            BuscarTipoUsuarioUseCase buscarTipoUsuarioUseCase,
            ListarTipoUsuarioUseCase listarTipoUsuarioUseCase,
            AssociarUsuarioUseCase associarUsuarioUseCase) {
        this.criarTipoUsuarioUseCase = criarTipoUsuarioUseCase;
        this.atualizarTipoUsuarioUseCase = atualizarTipoUsuarioUseCase;
        this.excluirTipoUsuarioUseCase = excluirTipoUsuarioUseCase;
        this.buscarTipoUsuarioUseCase = buscarTipoUsuarioUseCase;
        this.listarTipoUsuarioUseCase = listarTipoUsuarioUseCase;
        this.associarUsuarioUseCase = associarUsuarioUseCase;
    }

    @Operation(summary = "Cadastrar tipo de usuário", description = "Cria um novo tipo de usuário (ex: CLIENTE, DONO_RESTAURANTE).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo de usuário criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<TipoUsuarioResponseDTO> criar(@Valid @RequestBody TipoUsuarioCreateDTO dto) {
        TipoUsuarioResponseDTO criado = criarTipoUsuarioUseCase.executar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @Operation(summary = "Atualizar tipo de usuário", description = "Atualiza o nome de um tipo de usuário existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de usuário atualizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> atualizar(
            @Parameter(description = "ID do tipo de usuário") @PathVariable Long id,
            @Valid @RequestBody TipoUsuarioCreateDTO dto) {
        TipoUsuarioResponseDTO atualizado = atualizarTipoUsuarioUseCase.executar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Excluir tipo de usuário", description = "Remove um tipo de usuário existente pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo de usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@Parameter(description = "ID do tipo de usuário") @PathVariable Long id) {
        excluirTipoUsuarioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar tipo de usuário por ID", description = "Retorna os dados de um tipo de usuário a partir do seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de usuário encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de usuário não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> buscarPorId(@Parameter(description = "ID do tipo de usuário") @PathVariable Long id) {
        return ResponseEntity.ok(buscarTipoUsuarioUseCase.executar(id));
    }

    @Operation(summary = "Listar tipos de usuário", description = "Retorna todos os tipos de usuário cadastrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TipoUsuarioResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<TipoUsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(listarTipoUsuarioUseCase.executar());
    }

    @Operation(summary = "Associar tipo de usuário a um usuário", description = "Associa um tipo de usuário já cadastrado a um usuário existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Associação realizada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário ou tipo de usuário não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PatchMapping("/{tipoUsuarioId}/usuarios/{usuarioId}")
    public ResponseEntity<UsuarioResponseDTO> associarUsuario(
            @Parameter(description = "ID do tipo de usuário") @PathVariable Long tipoUsuarioId,
            @Parameter(description = "ID do usuário") @PathVariable Long usuarioId) {
        return ResponseEntity.ok(associarUsuarioUseCase.executar(usuarioId, tipoUsuarioId));
    }
}
