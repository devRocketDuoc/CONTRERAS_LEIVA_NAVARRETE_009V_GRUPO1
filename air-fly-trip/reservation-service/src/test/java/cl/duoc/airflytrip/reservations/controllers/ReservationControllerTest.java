package cl.duoc.airflytrip.reservations.controllers;

import cl.duoc.airflytrip.reservations.dtos.request.CreateReservationRequest;
import cl.duoc.airflytrip.reservations.dtos.request.UpdateReservationStatusRequest;
import cl.duoc.airflytrip.reservations.dtos.response.ReservationResponse;
import cl.duoc.airflytrip.reservations.services.ReservationService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    void createShouldReturnCreatedReservation() {

        CreateReservationRequest request = new CreateReservationRequest();
        request.setUserId(12L);
        request.setRouteId(8L);
        request.setOriginTerminalId(3L);
        request.setDestinationTerminalId(5L);
        request.setReservedAt(LocalDateTime.of(2026, 7, 15, 14, 30));

        ReservationResponse response = reservationResponse(40L, "PENDING", true);

        when(reservationService.create(any(CreateReservationRequest.class))).thenReturn(response);

        ResponseEntity<ReservationResponse> result = reservationController.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(40L, result.getBody().getId());
        assertEquals("PENDING", result.getBody().getStatus());
    }

    @Test
    void findAllShouldReturnReservationList() {

        when(reservationService.findAll()).thenReturn(List.of(
                reservationResponse(40L, "PENDING", true),
                reservationResponse(41L, "CONFIRMED", true)
        ));

        ResponseEntity<List<ReservationResponse>> result = reservationController.findAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void updateStatusShouldReturnUpdatedReservation() {

        UpdateReservationStatusRequest request = new UpdateReservationStatusRequest();
        request.setStatus("CONFIRMED");

        ReservationResponse response = reservationResponse(40L, "CONFIRMED", true);

        when(reservationService.updateStatus(eq(40L), any(UpdateReservationStatusRequest.class))).thenReturn(response);

        ResponseEntity<ReservationResponse> result = reservationController.updateStatus(40L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("CONFIRMED", result.getBody().getStatus());
    }

    @Test
    void deleteShouldReturnNoContent() {

        doNothing().when(reservationService).delete(40L);

        ResponseEntity<Void> result = reservationController.delete(40L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    private ReservationResponse reservationResponse(Long id, String status, Boolean active) {
        return ReservationResponse.builder()
                .id(id)
                .userId(12L)
                .routeId(8L)
                .originTerminalId(3L)
                .destinationTerminalId(5L)
                .reservedAt(LocalDateTime.of(2026, 7, 15, 14, 30))
                .status(status)
                .active(active)
                .createdAt(LocalDateTime.of(2026, 6, 21, 10, 15))
                .build();
    }
}
