package cl.duoc.airflytrip.chargingstations.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.airflytrip.chargingstations.dtos.request.CreateChargingStationRequest;
import cl.duoc.airflytrip.chargingstations.dtos.request.UpdateChargingStationRequest;
import cl.duoc.airflytrip.chargingstations.dtos.request.UpdateChargingStationStatusRequest;
import cl.duoc.airflytrip.chargingstations.dtos.response.ChargingStationResponse;
import cl.duoc.airflytrip.chargingstations.dtos.response.ErrorResponse;
import cl.duoc.airflytrip.chargingstations.services.ChargingStationService;
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
@RequestMapping("/api/v1/charging-stations")
@RequiredArgsConstructor
@Tag(
        name = "Estaciones de carga",
        description = "Operaciones para consultar y administrar las estaciones de carga del sistema."
)
public class ChargingStationController {

    private final ChargingStationService chargingStationService;

    @GetMapping
    @Operation(
            summary = "Listar todas las estaciones de carga",
            description = "Retorna la lista completa de estaciones de carga registradas en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estaciones de carga obtenidas correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ChargingStationResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<ChargingStationResponse>> findAll() {
        return ResponseEntity.ok(chargingStationService.findAll());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Listar estaciones de carga activas",
            description = "Retorna las estaciones de carga que se encuentran activas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estaciones de carga activas obtenidas correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ChargingStationResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<ChargingStationResponse>> findActive() {
        return ResponseEntity.ok(chargingStationService.findActive());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener una estacion de carga por identificador",
            description = "Retorna la informacion de una estacion de carga a partir de su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estacion de carga obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChargingStationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ChargingStationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(chargingStationService.findById(id));
    }

    @GetMapping("/terminal/{terminalId}")
    @Operation(
            summary = "Listar estaciones de carga por terminal",
            description = "Retorna las estaciones de carga asociadas a un terminal especifico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estaciones de carga del terminal obtenidas correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ChargingStationResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<ChargingStationResponse>> findByTerminalId(@PathVariable Long terminalId) {
        return ResponseEntity.ok(chargingStationService.findByTerminalId(terminalId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Crear una estacion de carga",
            description = "Registra una nueva estacion de carga con los datos enviados en la solicitud."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Estacion de carga creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChargingStationResponse.class))
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
                    description = "El usuario autenticado no tiene permisos para crear estaciones de carga",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ChargingStationResponse> create(@Valid @RequestBody CreateChargingStationRequest request) {
        ChargingStationResponse response = chargingStationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Actualizar una estacion de carga",
            description = "Actualiza la informacion completa de una estacion de carga existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estacion de carga actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChargingStationResponse.class))
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
                    description = "El usuario autenticado no tiene permisos para actualizar estaciones de carga",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ChargingStationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChargingStationRequest request
    ) {
        return ResponseEntity.ok(chargingStationService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @Operation(
            summary = "Actualizar estado de una estacion de carga",
            description = "Modifica el estado operativo de una estacion de carga existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado de la estacion de carga actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChargingStationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud invalida por errores de validacion o estado incorrecto",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene permisos para actualizar el estado de la estacion de carga",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ChargingStationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChargingStationStatusRequest request
    ) {
        return ResponseEntity.ok(chargingStationService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Eliminar una estacion de carga",
            description = "Elimina una estacion de carga existente a partir de su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Estacion de carga eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene permisos para eliminar estaciones de carga",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chargingStationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
