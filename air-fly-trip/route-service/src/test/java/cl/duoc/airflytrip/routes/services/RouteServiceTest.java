package cl.duoc.airflytrip.routes.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cl.duoc.airflytrip.routes.clients.TerminalClient;
import cl.duoc.airflytrip.routes.clients.response.TerminalResponse;
import cl.duoc.airflytrip.routes.dtos.request.CreateRouteRequest;
import cl.duoc.airflytrip.routes.dtos.request.UpdateRouteRequest;
import cl.duoc.airflytrip.routes.dtos.response.RouteResponse;
import cl.duoc.airflytrip.routes.exceptions.BadRequestException;
import cl.duoc.airflytrip.routes.models.Route;
import cl.duoc.airflytrip.routes.repositories.RouteRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

  @Mock
  private RouteRepository routeRepository;

  @Mock
  private TerminalClient terminalClient;

  @InjectMocks
  private RouteService routeService;

  @Test
  void testCrearRutaExitosa() {

    CreateRouteRequest request = new CreateRouteRequest();
    request.setOriginTerminalId(10L);
    request.setDestinationTerminalId(20L);
    request.setDistanceKm(new BigDecimal("15.75"));
    request.setEstimatedMinutes(35);

    when(terminalClient.findById(10L)).thenReturn(terminalResponse(10L, true));
    when(terminalClient.findById(20L)).thenReturn(terminalResponse(20L, true));
    when(routeRepository.existsByOriginTerminalIdAndDestinationTerminalId(10L, 20L)).thenReturn(false);
    when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> {
      Route route = invocation.getArgument(0);
      route.setId(100L);
      return route;
    });

    RouteResponse response = routeService.create(request);

    assertNotNull(response);
    assertEquals(100L, response.getId());
    assertEquals(10L, response.getOriginTerminalId());
    assertEquals(20L, response.getDestinationTerminalId());
    assertTrue(response.getActive());
  }

  @Test
  void testCrearRutaFallaMismoOrigenDestino() {

    CreateRouteRequest request = new CreateRouteRequest();
    request.setOriginTerminalId(10L);
    request.setDestinationTerminalId(10L);
    request.setDistanceKm(new BigDecimal("15.75"));
    request.setEstimatedMinutes(35);

    Exception exception = null;

    try {
      routeService.create(request);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(BadRequestException.class, exception.getClass());
    assertEquals("Origin and destination terminals cannot be the same", exception.getMessage());
  }

  @Test
  void testActualizarRutaMantieneActivoCuandoNoSeInforma() {

    UpdateRouteRequest request = new UpdateRouteRequest();
    request.setOriginTerminalId(11L);
    request.setDestinationTerminalId(21L);
    request.setDistanceKm(new BigDecimal("18.40"));
    request.setEstimatedMinutes(40);

    Route existing = route(50L, 10L, 20L, "10.00", 25, true);

    when(routeRepository.findById(50L)).thenReturn(Optional.of(existing));
    when(terminalClient.findById(11L)).thenReturn(terminalResponse(11L, true));
    when(terminalClient.findById(21L)).thenReturn(terminalResponse(21L, true));
    when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));

    RouteResponse response = routeService.update(50L, request);

    assertNotNull(response);
    assertEquals(50L, response.getId());
    assertEquals(11L, response.getOriginTerminalId());
    assertTrue(response.getActive());
  }

  @Test
  void testEliminarRutaDesactivaRegistro() {

    Route existing = route(60L, 10L, 20L, "10.00", 25, true);

    when(routeRepository.findById(60L)).thenReturn(Optional.of(existing));
    when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));

    routeService.delete(60L);

    assertFalse(existing.getActive());
  }

  private Route route(Long id, Long origin, Long destination, String distance, Integer minutes, Boolean active) {
    return Route.builder()
        .id(id)
        .originTerminalId(origin)
        .destinationTerminalId(destination)
        .distanceKm(new BigDecimal(distance))
        .estimatedMinutes(minutes)
        .active(active)
        .build();
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
}
