package cl.duoc.airflytrip.payments.controllers;

import cl.duoc.airflytrip.payments.dtos.request.CreatePaymentRequest;
import cl.duoc.airflytrip.payments.dtos.request.UpdatePaymentStatusRequest;
import cl.duoc.airflytrip.payments.dtos.response.PaymentResponse;
import cl.duoc.airflytrip.payments.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<PaymentResponse>> findAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<PaymentResponse>> findByTripId(@PathVariable Long tripId) {
        return ResponseEntity.ok(paymentService.findByTripId(tripId));
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<PaymentResponse>> findByReservationId(@PathVariable Long reservationId) {
        return ResponseEntity.ok(paymentService.findByReservationId(reservationId));
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    public ResponseEntity<PaymentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentStatusRequest request
    ) {
        return ResponseEntity.ok(paymentService.updateStatus(id, request));
    }
}
