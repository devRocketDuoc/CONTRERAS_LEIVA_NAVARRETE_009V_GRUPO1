package cl.duoc.airflytrip.terminals.controllers;

import java.util.List;

import cl.duoc.airflytrip.terminals.dtos.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.airflytrip.terminals.dtos.request.CreateTerminalRequest;
import cl.duoc.airflytrip.terminals.dtos.request.UpdateTerminalRequest;
import cl.duoc.airflytrip.terminals.dtos.response.TerminalResponse;
import cl.duoc.airflytrip.terminals.services.TerminalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/terminals")
@RequiredArgsConstructor
@Tag(
        name = "Terminales",
        description = "Operaciones para consultar y administrar los terminales del sistema."
)
public class TerminalController {

    private final TerminalService terminalService;

    @GetMapping
    @Operation(
            summary = "Listar todos los terminales",
            description = "Retorna la lista completa de terminales registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Terminales obtenidos correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TerminalResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<TerminalResponse>> findAll() {
        return ResponseEntity.ok(terminalService.findAll());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Listar terminales activos",
            description = "Retorna los terminales que se encuentran marcados como activos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Terminales activos obtenidos correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TerminalResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<TerminalResponse>> findActive() {
        return ResponseEntity.ok(terminalService.findActive());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un terminal por identificador",
            description = "Retorna la informacion de un terminal a partir de su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Terminal obtenido correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TerminalResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TerminalResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(terminalService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Crear un terminal",
            description = "Registra un nuevo terminal con los datos enviados en la solicitud."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Terminal creado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TerminalResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud invalida por errores de validacion o datos incorrectos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene permisos para crear terminales",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TerminalResponse> create(@Valid @RequestBody CreateTerminalRequest request) {
        TerminalResponse response = terminalService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Actualizar un terminal",
            description = "Actualiza la informacion completa de un terminal existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Terminal actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TerminalResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud invalida por errores de validacion o datos incorrectos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene permisos para actualizar terminales",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TerminalResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTerminalRequest request
    ) {
        return ResponseEntity.ok(terminalService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Eliminar un terminal",
            description = "Elimina un terminal existente a partir de su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Terminal eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene permisos para eliminar terminales",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        terminalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
