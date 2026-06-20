package cl.duoc.airflytrip.payments.controllers;

import cl.duoc.airflytrip.payments.dtos.request.CreatePaymentRequest;
import cl.duoc.airflytrip.payments.dtos.request.UpdatePaymentStatusRequest;
import cl.duoc.airflytrip.payments.dtos.response.ErrorResponse;
import cl.duoc.airflytrip.payments.dtos.response.PaymentResponse;
import cl.duoc.airflytrip.payments.services.PaymentService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(
        name = "Pagos",
        description = "Operaciones para consultar y administrar los pagos registrados en el sistema."
)
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @Operation(
            summary = "Listar todos los pagos",
            description = "Retorna la lista completa de pagos registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagos obtenidos correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PaymentResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El usuario autenticado no tiene permisos para consultar todos los pagos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<PaymentResponse>> findAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un pago por identificador",
            description = "Retorna la informacion de un pago a partir de su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pago obtenido correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<PaymentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping("/trip/{tripId}")
    @Operation(
            summary = "Listar pagos por viaje",
            description = "Retorna los pagos asociados a un viaje especifico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagos del viaje obtenidos correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PaymentResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<PaymentResponse>> findByTripId(@PathVariable Long tripId) {
        return ResponseEntity.ok(paymentService.findByTripId(tripId));
    }

    @GetMapping("/reservation/{reservationId}")
    @Operation(
            summary = "Listar pagos por reserva",
            description = "Retorna los pagos asociados a una reserva especifica."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Pagos de la reserva obtenidos correctamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PaymentResponse.class)))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token ausente, invalido o expirado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<List<PaymentResponse>> findByReservationId(@PathVariable Long reservationId) {
        return ResponseEntity.ok(paymentService.findByReservationId(reservationId));
    }

    @PostMapping
    @Operation(
            summary = "Crear un pago",
            description = "Registra un nuevo pago con los datos enviados en la solicitud."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pago creado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))
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
            )
    })
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest request) {
        PaymentResponse response = paymentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OPERATOR')")
    @Operation(
            summary = "Actualizar estado de un pago",
            description = "Modifica el estado de un pago existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado del pago actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class))
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
                    description = "El usuario autenticado no tiene permisos para actualizar el estado del pago",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<PaymentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentStatusRequest request
    ) {
        return ResponseEntity.ok(paymentService.updateStatus(id, request));
    }
}
