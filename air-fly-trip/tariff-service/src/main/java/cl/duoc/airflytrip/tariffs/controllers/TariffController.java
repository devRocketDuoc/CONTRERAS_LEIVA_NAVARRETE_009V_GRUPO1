package cl.duoc.airflytrip.tariffs.controllers;

import cl.duoc.airflytrip.tariffs.dtos.request.CreateTariffRequest;
import cl.duoc.airflytrip.tariffs.dtos.request.UpdateTariffRequest;
import cl.duoc.airflytrip.tariffs.dtos.response.ErrorResponse;
import cl.duoc.airflytrip.tariffs.dtos.response.TariffResponse;
import cl.duoc.airflytrip.tariffs.services.TariffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/tariffs")
@RequiredArgsConstructor
@Tag(
        name = "Tarifas",
        description = "Operaciones para consultar y administrar las tarifas configuradas para las rutas del sistema."
)
public class TariffController {

    private final TariffService tariffService;

    @GetMapping
    @Operation(
            summary = "Listar todas las tarifas",
            description = "Retorna la lista completa de tarifas registradas en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarifas obtenidas correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TariffResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<TariffResponse>> findAll() {
        return ResponseEntity.ok(tariffService.findAll());
    }

    @GetMapping("/active")
    @Operation(
            summary = "Listar tarifas activas",
            description = "Retorna las tarifas que se encuentran marcadas como activas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarifas activas obtenidas correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TariffResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<TariffResponse>> findActive() {
        return ResponseEntity.ok(tariffService.findActive());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener una tarifa por identificador",
            description = "Retorna la informacion de una tarifa a partir de su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarifa obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TariffResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TariffResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tariffService.findById(id));
    }

    @GetMapping("/route/{routeId}")
    @Operation(
            summary = "Listar tarifas por ruta",
            description = "Retorna las tarifas asociadas a la ruta indicada en la solicitud."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarifas de la ruta obtenidas correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TariffResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud invalida por ruta inexistente o inactiva",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<TariffResponse>> findByRouteId(@PathVariable Long routeId) {
        return ResponseEntity.ok(tariffService.findByRouteId(routeId));
    }

    @GetMapping("/route/{routeId}/vehicle-type/{vehicleType}")
    @Operation(
            summary = "Obtener una tarifa por ruta y tipo de vehiculo",
            description = "Retorna la tarifa configurada para una combinacion especifica de ruta y tipo de vehiculo."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarifa obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TariffResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TariffResponse> findByRouteIdAndVehicleType(
            @PathVariable Long routeId,
            @PathVariable String vehicleType
    ) {
        return ResponseEntity.ok(tariffService.findByRouteIdAndVehicleType(routeId, vehicleType));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Crear una tarifa",
            description = "Registra una nueva tarifa para una ruta y un tipo de vehiculo determinados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Tarifa creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TariffResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud invalida por errores de validacion, ruta incorrecta o tarifa duplicada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene permisos para crear tarifas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TariffResponse> create(@Valid @RequestBody CreateTariffRequest request) {
        TariffResponse response = tariffService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Actualizar una tarifa",
            description = "Actualiza la informacion completa de una tarifa existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tarifa actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TariffResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud invalida por errores de validacion o datos inconsistentes",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene permisos para actualizar tarifas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<TariffResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTariffRequest request
    ) {
        return ResponseEntity.ok(tariffService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Desactivar una tarifa",
            description = "Marca una tarifa existente como inactiva en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Tarifa desactivada correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene permisos para desactivar tarifas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tariffService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
