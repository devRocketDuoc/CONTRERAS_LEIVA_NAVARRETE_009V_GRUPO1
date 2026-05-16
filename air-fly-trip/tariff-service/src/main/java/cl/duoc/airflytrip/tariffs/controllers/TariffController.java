package cl.duoc.airflytrip.tariffs.controllers;

import cl.duoc.airflytrip.tariffs.dtos.request.CreateTariffRequest;
import cl.duoc.airflytrip.tariffs.dtos.request.UpdateTariffRequest;
import cl.duoc.airflytrip.tariffs.dtos.response.TariffResponse;
import cl.duoc.airflytrip.tariffs.services.TariffService;
import jakarta.validation.Valid;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/tariffs")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    @GetMapping
    public ResponseEntity<List<TariffResponse>> findAll() {
        return ResponseEntity.ok(tariffService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<TariffResponse>> findActive() {
        return ResponseEntity.ok(tariffService.findActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tariffService.findById(id));
    }

    @GetMapping("/route/{routeId}")
    public ResponseEntity<List<TariffResponse>> findByRouteId(@PathVariable Long routeId) {
        return ResponseEntity.ok(tariffService.findByRouteId(routeId));
    }

    @GetMapping("/route/{routeId}/vehicle-type/{vehicleType}")
    public ResponseEntity<TariffResponse> findByRouteIdAndVehicleType(
            @PathVariable Long routeId,
            @PathVariable String vehicleType
    ) {
        return ResponseEntity.ok(tariffService.findByRouteIdAndVehicleType(routeId, vehicleType));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TariffResponse> create(@Valid @RequestBody CreateTariffRequest request) {
        TariffResponse response = tariffService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TariffResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTariffRequest request
    ) {
        return ResponseEntity.ok(tariffService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tariffService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
