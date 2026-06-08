package cl.duoc.airflytrip.routes.controllers;

import java.util.List;

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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/routes")
@Tag(name = "Rutas", description = "Operaciones relacionadas con la ruta")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;
    @Operation(summary = "Obtener todos las rutas", description="Obtiene una lista de todas las rutas")
    @ApiResponses(value = {
        @ApiResponse( responseCode = "200", description = "Exito"),
        @ApiResponse( responseCode = "404", description = "Sin las rutas")
    })
    @GetMapping
    public ResponseEntity<List<RouteResponse>> findAll() {
        return ResponseEntity.ok(routeService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<RouteResponse>> findActive() {
        return ResponseEntity.ok(routeService.findActive());
    }

    @Operation(summary = "Obtener la ruta por id", description="Obtiene una ruta por id")

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.findById(id));
    }

    @GetMapping("/origin/{originTerminalId}/destination/{destinationTerminalId}")
    public ResponseEntity<RouteResponse> findByOriginAndDestination(
            @PathVariable Long originTerminalId,
            @PathVariable Long destinationTerminalId
    ) {
        return ResponseEntity.ok(routeService.findByOriginAndDestination(originTerminalId, destinationTerminalId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RouteResponse> create(@Valid @RequestBody CreateRouteRequest request) {
        RouteResponse response = routeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RouteResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRouteRequest request
    ) {
        return ResponseEntity.ok(routeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
