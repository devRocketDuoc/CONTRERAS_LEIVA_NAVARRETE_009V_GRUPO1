package cl.duoc.airflytrip.trips.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cl.duoc.airflytrip.trips.clients.AuthClient;
import cl.duoc.airflytrip.trips.clients.RouteClient;
import cl.duoc.airflytrip.trips.clients.TerminalClient;
import cl.duoc.airflytrip.trips.clients.VehicleClient;
import cl.duoc.airflytrip.trips.clients.response.RouteResponse;
import cl.duoc.airflytrip.trips.clients.response.TerminalResponse;
import cl.duoc.airflytrip.trips.clients.response.UserResponse;
import cl.duoc.airflytrip.trips.clients.response.VehicleResponse;
import cl.duoc.airflytrip.trips.dtos.request.CreateTripRequest;
import cl.duoc.airflytrip.trips.dtos.request.UpdateTripStatusRequest;
import cl.duoc.airflytrip.trips.dtos.response.TripResponse;
import cl.duoc.airflytrip.trips.exceptions.BadRequestException;
import cl.duoc.airflytrip.trips.models.Trip;
import cl.duoc.airflytrip.trips.repositories.TripRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TripServiceTest {

  @Mock
  private TripRepository tripRepository;

  @Mock
  private AuthClient authClient;

  @Mock
  private TerminalClient terminalClient;

  @Mock
  private VehicleClient vehicleClient;

  @Mock
  private RouteClient routeClient;

  @InjectMocks
  private TripService tripService;

  @Test
  void testCrearViajeExitoso() {

    CreateTripRequest request = new CreateTripRequest();
    request.setUserId(15L);
    request.setVehicleId(8L);
    request.setOriginTerminalId(2L);
    request.setDestinationTerminalId(5L);
    request.setRouteId(11L);
    request.setScheduledAt(LocalDateTime.now().plusHours(2));

    when(authClient.findUserById(15L)).thenReturn(userResponse(15L, true));
    when(terminalClient.findById(2L)).thenReturn(terminalResponse(2L, true));
    when(terminalClient.findById(5L)).thenReturn(terminalResponse(5L, true));
    when(routeClient.findById(11L)).thenReturn(routeResponse(11L, 2L, 5L, true));
    when(vehicleClient.findById(8L)).thenReturn(vehicleResponse(8L, true));
    when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> {
      Trip trip = invocation.getArgument(0);
      trip.setId(40L);
      return trip;
    });

    TripResponse response = tripService.create(request);

    assertNotNull(response);
    assertEquals(40L, response.getId());
    assertEquals("REQUESTED", response.getStatus());
    assertTrue(response.getActive());
    assertEquals(11L, response.getRouteId());
  }

  @Test
  void testCrearViajeFallaRutaNoCoincide() {

    CreateTripRequest request = new CreateTripRequest();
    request.setUserId(15L);
    request.setOriginTerminalId(2L);
    request.setDestinationTerminalId(5L);
    request.setRouteId(11L);
    request.setScheduledAt(LocalDateTime.now().plusHours(2));

    when(authClient.findUserById(15L)).thenReturn(userResponse(15L, true));
    when(terminalClient.findById(2L)).thenReturn(terminalResponse(2L, true));
    when(terminalClient.findById(5L)).thenReturn(terminalResponse(5L, true));
    when(routeClient.findById(11L)).thenReturn(routeResponse(11L, 2L, 9L, true));

    Exception exception = null;

    try {
      tripService.create(request);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(BadRequestException.class, exception.getClass());
    assertEquals("Route does not match origin and destination terminals", exception.getMessage());
  }

  @Test
  void testActualizarEstadoCanceladoDesactivaViaje() {

    UpdateTripStatusRequest request = new UpdateTripStatusRequest();
    request.setStatus("CANCELLED");

    Trip existing = trip(24L, "REQUESTED", true);

    when(tripRepository.findById(24L)).thenReturn(Optional.of(existing));
    when(tripRepository.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

    TripResponse response = tripService.updateStatus(24L, request);

    assertNotNull(response);
    assertEquals("CANCELLED", response.getStatus());
    assertFalse(response.getActive());
  }

  @Test
  void testFinalizarViajeFallaSiNoEstaEnCurso() {

    when(tripRepository.findById(24L)).thenReturn(Optional.of(trip(24L, "REQUESTED", true)));

    Exception exception = null;

    try {
      tripService.finishTrip(24L);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(BadRequestException.class, exception.getClass());
    assertEquals("Only trips in progress can be finished", exception.getMessage());
  }

  private Trip trip(Long id, String status, Boolean active) {
    return Trip.builder()
        .id(id)
        .userId(15L)
        .vehicleId(8L)
        .originTerminalId(2L)
        .destinationTerminalId(5L)
        .routeId(11L)
        .status(status)
        .scheduledAt(LocalDateTime.of(2026, 6, 21, 9, 30))
        .startedAt(null)
        .finishedAt(null)
        .active(active)
        .createdAt(LocalDateTime.of(2026, 6, 20, 18, 10))
        .build();
  }

  private UserResponse userResponse(Long id, Boolean enabled) {
    UserResponse response = new UserResponse();
    response.setId(id);
    response.setEmail("user@example.com");
    response.setFirstName("Ana");
    response.setLastName("Perez");
    response.setEnabled(enabled);
    return response;
  }

  private TerminalResponse terminalResponse(Long id, Boolean active) {
    TerminalResponse response = new TerminalResponse();
    response.setId(id);
    response.setName("Terminal " + id);
    response.setCity("Santiago");
    response.setLocationDescription("Ubicacion " + id);
    response.setActive(active);
    return response;
  }

  private VehicleResponse vehicleResponse(Long id, Boolean active) {
    VehicleResponse response = new VehicleResponse();
    response.setId(id);
    response.setCode("VEH-00" + id);
    response.setModel("BYD Dolphin");
    response.setStatus("AVAILABLE");
    response.setBatteryPercentage(80);
    response.setTerminalId(2L);
    response.setChargingStationId(7L);
    response.setActive(active);
    return response;
  }

  private RouteResponse routeResponse(Long id, Long originTerminalId, Long destinationTerminalId, Boolean active) {
    RouteResponse response = new RouteResponse();
    response.setId(id);
    response.setOriginTerminalId(originTerminalId);
    response.setDestinationTerminalId(destinationTerminalId);
    response.setDistanceKm(new java.math.BigDecimal("12.5"));
    response.setEstimatedMinutes(35);
    response.setActive(active);
    return response;
  }
}
