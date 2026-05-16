package cl.duoc.airflytrip.vehicles.controllers;

import cl.duoc.airflytrip.vehicles.dtos.request.CreateVehicleRequest;
import cl.duoc.airflytrip.vehicles.dtos.request.UpdateVehicleBatteryRequest;
import cl.duoc.airflytrip.vehicles.dtos.request.UpdateVehicleRequest;
import cl.duoc.airflytrip.vehicles.dtos.request.UpdateVehicleStatusRequest;
import cl.duoc.airflytrip.vehicles.dtos.response.VehicleResponse;
import cl.duoc.airflytrip.vehicles.services.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> findAll() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<VehicleResponse>> findActive() {
        return ResponseEntity.ok(vehicleService.findActive());
    }

    @GetMapping("/available")
    public ResponseEntity<List<VehicleResponse>> findAvailable() {
        return ResponseEntity.ok(vehicleService.findAvailable());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @GetMapping("/terminal/{terminalId}")
    public ResponseEntity<List<VehicleResponse>> findByTerminalId(@PathVariable Long terminalId) {
        return ResponseEntity.ok(vehicleService.findByTerminalId(terminalId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody CreateVehicleRequest request) {
        VehicleResponse response = vehicleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<VehicleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request
    ) {
        return ResponseEntity.ok(vehicleService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<VehicleResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleStatusRequest request
    ) {
        return ResponseEntity.ok(vehicleService.updateStatus(id, request));
    }

    @PatchMapping("/{id}/battery")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<VehicleResponse> updateBattery(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleBatteryRequest request
    ) {
        return ResponseEntity.ok(vehicleService.updateBattery(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
