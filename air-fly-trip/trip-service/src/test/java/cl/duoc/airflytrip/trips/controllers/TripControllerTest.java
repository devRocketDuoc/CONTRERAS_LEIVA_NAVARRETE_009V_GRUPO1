package cl.duoc.airflytrip.trips.controllers;

import cl.duoc.airflytrip.trips.dtos.request.CreateTripRequest;
import cl.duoc.airflytrip.trips.dtos.request.UpdateTripStatusRequest;
import cl.duoc.airflytrip.trips.dtos.response.TripResponse;
import cl.duoc.airflytrip.trips.services.TripService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripControllerTest {

  @Mock
  private TripService tripService;

  @InjectMocks
  private TripController tripController;

  @Test
  void findAllShouldReturnTrips() {

    when(tripService.findAll()).thenReturn(List.of(tripResponse(1L, "REQUESTED", true)));

    ResponseEntity<List<TripResponse>> result = tripController.findAll();

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(1, result.getBody().size());
    assertEquals(1L, result.getBody().get(0).getId());
  }

  @Test
  void createShouldReturnCreatedTrip() {

    CreateTripRequest request = new CreateTripRequest();
    request.setUserId(15L);
    request.setVehicleId(8L);
    request.setOriginTerminalId(2L);
    request.setDestinationTerminalId(5L);
    request.setRouteId(11L);
    request.setScheduledAt(LocalDateTime.now().plusHours(2));

    TripResponse responseSimulado = tripResponse(24L, "REQUESTED", true);

    when(tripService.create(any(CreateTripRequest.class))).thenReturn(responseSimulado);

    ResponseEntity<TripResponse> result = tripController.create(request);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(24L, result.getBody().getId());
    assertEquals("REQUESTED", result.getBody().getStatus());
  }

  @Test
  void updateStatusShouldReturnUpdatedTrip() {

    UpdateTripStatusRequest request = new UpdateTripStatusRequest();
    request.setStatus("CANCELLED");

    TripResponse responseSimulado = tripResponse(24L, "CANCELLED", false);

    when(tripService.updateStatus(24L, request)).thenReturn(responseSimulado);

    ResponseEntity<TripResponse> result = tripController.updateStatus(24L, request);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals("CANCELLED", result.getBody().getStatus());
    assertEquals(false, result.getBody().getActive());
  }

  @Test
  void deleteShouldReturnNoContent() {

    ResponseEntity<Void> result = tripController.delete(30L);

    assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
  }

  private TripResponse tripResponse(Long id, String status, Boolean active) {
    LocalDateTime scheduledAt = LocalDateTime.of(2026, 6, 21, 9, 30);
    return TripResponse.builder()
        .id(id)
        .userId(15L)
        .vehicleId(8L)
        .originTerminalId(2L)
        .destinationTerminalId(5L)
        .routeId(11L)
        .status(status)
        .scheduledAt(scheduledAt)
        .startedAt(null)
        .finishedAt(null)
        .active(active)
        .createdAt(LocalDateTime.of(2026, 6, 20, 18, 10))
        .build();
  }
}
