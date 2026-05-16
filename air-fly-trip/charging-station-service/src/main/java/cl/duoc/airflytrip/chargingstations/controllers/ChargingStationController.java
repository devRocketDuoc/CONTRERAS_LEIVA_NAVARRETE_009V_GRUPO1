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
import cl.duoc.airflytrip.chargingstations.services.ChargingStationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/charging-stations")
@RequiredArgsConstructor
public class ChargingStationController {

    private final ChargingStationService chargingStationService;

    @GetMapping
    public ResponseEntity<List<ChargingStationResponse>> findAll() {
        return ResponseEntity.ok(chargingStationService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ChargingStationResponse>> findActive() {
        return ResponseEntity.ok(chargingStationService.findActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargingStationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(chargingStationService.findById(id));
    }

    @GetMapping("/terminal/{terminalId}")
    public ResponseEntity<List<ChargingStationResponse>> findByTerminalId(@PathVariable Long terminalId) {
        return ResponseEntity.ok(chargingStationService.findByTerminalId(terminalId));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ChargingStationResponse> create(@Valid @RequestBody CreateChargingStationRequest request) {
        ChargingStationResponse response = chargingStationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ChargingStationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChargingStationRequest request
    ) {
        return ResponseEntity.ok(chargingStationService.update(id, request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<ChargingStationResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChargingStationStatusRequest request
    ) {
        return ResponseEntity.ok(chargingStationService.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chargingStationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
