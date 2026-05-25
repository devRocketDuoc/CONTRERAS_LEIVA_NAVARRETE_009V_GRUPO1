package cl.duoc.airflytrip.trips.controllers;

import cl.duoc.airflytrip.trips.dtos.request.CreateTripRequest;
import cl.duoc.airflytrip.trips.dtos.request.UpdateTripStatusRequest;
import cl.duoc.airflytrip.trips.dtos.response.TripResponse;
import cl.duoc.airflytrip.trips.services.TripService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<TripResponse>> findAll() {
        return ResponseEntity.ok(tripService.findAll());
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<TripResponse>> findActive() {
        return ResponseEntity.ok(tripService.findActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TripResponse>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(tripService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<TripResponse> create(@Valid @RequestBody CreateTripRequest request) {
        TripResponse response = tripService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<TripResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTripStatusRequest request
    ) {
        return ResponseEntity.ok(tripService.updateStatus(id, request));
    }

    @PatchMapping("/{id}/start")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<TripResponse> startTrip(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.startTrip(id));
    }

    @PatchMapping("/{id}/finish")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<TripResponse> finishTrip(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.finishTrip(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

