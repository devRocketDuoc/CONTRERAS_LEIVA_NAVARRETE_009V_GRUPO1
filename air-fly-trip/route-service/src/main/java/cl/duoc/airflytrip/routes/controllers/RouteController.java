package cl.duoc.airflytrip.routes.controllers;

import java.util.List;

import cl.duoc.airflytrip.routes.dtos.response.ErrorResponse;
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

import cl.duoc.airflytrip.routes.dtos.request.CreateRouteRequest;
import cl.duoc.airflytrip.routes.dtos.request.UpdateRouteRequest;
import cl.duoc.airflytrip.routes.dtos.response.RouteResponse;
import cl.duoc.airflytrip.routes.services.RouteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/routes")
@Tag(
        name = "Rutas",
        description = "Operaciones para consultar y administrar las rutas disponibles en el sistema."
)
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping
    @Operation(
            summary = "Listar todas las rutas",
            description = "Retorna la lista completa de rutas registradas en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Rutas obtenidas correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RouteResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<RouteResponse>> findAll() {
        return ResponseEntity.ok(routeService.findAll());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Listar rutas activas",
            description = "Retorna las rutas que se encuentran marcadas como activas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Rutas activas obtenidas correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = RouteResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<RouteResponse>> findActive() {
        return ResponseEntity.ok(routeService.findActive());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener una ruta por identificador",
            description = "Retorna la informacion de una ruta a partir de su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ruta obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RouteResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RouteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.findById(id));
    }

    @GetMapping("/origin/{originTerminalId}/destination/{destinationTerminalId}")
    @Operation(
            summary = "Buscar una ruta por origen y destino",
            description = "Retorna la ruta asociada a un terminal de origen y un terminal de destino especificos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ruta obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RouteResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RouteResponse> findByOriginAndDestination(
            @PathVariable Long originTerminalId,
            @PathVariable Long destinationTerminalId
    ) {
        return ResponseEntity.ok(routeService.findByOriginAndDestination(originTerminalId, destinationTerminalId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Crear una ruta",
            description = "Registra una nueva ruta con los datos enviados en la solicitud."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Ruta creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RouteResponse.class))
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
                    description = "El usuario autenticado no tiene permisos para crear rutas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RouteResponse> create(@Valid @RequestBody CreateRouteRequest request) {
        RouteResponse response = routeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Actualizar una ruta",
            description = "Actualiza la informacion completa de una ruta existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ruta actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RouteResponse.class))
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
                    description = "El usuario autenticado no tiene permisos para actualizar rutas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<RouteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRouteRequest request
    ) {
        return ResponseEntity.ok(routeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Eliminar una ruta",
            description = "Elimina una ruta existente a partir de su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Ruta eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene permisos para eliminar rutas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
