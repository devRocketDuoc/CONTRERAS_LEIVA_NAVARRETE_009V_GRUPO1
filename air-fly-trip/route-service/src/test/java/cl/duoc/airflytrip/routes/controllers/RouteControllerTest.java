package cl.duoc.airflytrip.routes.controllers;

import cl.duoc.airflytrip.routes.dtos.request.CreateRouteRequest;
import cl.duoc.airflytrip.routes.dtos.request.UpdateRouteRequest;
import cl.duoc.airflytrip.routes.dtos.response.RouteResponse;
import cl.duoc.airflytrip.routes.services.RouteService;
import java.math.BigDecimal;
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
class RouteControllerTest {

  @Mock
  private RouteService routeService;

  @InjectMocks
  private RouteController routeController;

  @Test
  void findAllShouldReturnRoutes() {

    when(routeService.findAll()).thenReturn(List.of(routeResponse(1L, true)));

    ResponseEntity<List<RouteResponse>> result = routeController.findAll();

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(1, result.getBody().size());
    assertEquals(1L, result.getBody().get(0).getId());
  }

  @Test
  void createShouldReturnCreatedRoute() {

    CreateRouteRequest request = new CreateRouteRequest();
    request.setOriginTerminalId(10L);
    request.setDestinationTerminalId(20L);
    request.setDistanceKm(new BigDecimal("12.50"));
    request.setEstimatedMinutes(30);

    RouteResponse responseSimulado = routeResponse(10L, true);

    when(routeService.create(any(CreateRouteRequest.class))).thenReturn(responseSimulado);

    ResponseEntity<RouteResponse> result = routeController.create(request);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(10L, result.getBody().getId());
    assertEquals(10L, result.getBody().getOriginTerminalId());
  }

  @Test
  void updateShouldReturnUpdatedRoute() {

    UpdateRouteRequest request = new UpdateRouteRequest();
    request.setOriginTerminalId(11L);
    request.setDestinationTerminalId(21L);
    request.setDistanceKm(new BigDecimal("14.25"));
    request.setEstimatedMinutes(35);
    request.setActive(false);

    RouteResponse responseSimulado = RouteResponse.builder()
        .id(5L)
        .originTerminalId(11L)
        .destinationTerminalId(21L)
        .distanceKm(new BigDecimal("14.25"))
        .estimatedMinutes(35)
        .active(false)
        .build();

    when(routeService.update(5L, request)).thenReturn(responseSimulado);

    ResponseEntity<RouteResponse> result = routeController.update(5L, request);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(false, result.getBody().getActive());
  }

  @Test
  void deleteShouldReturnNoContent() {

    ResponseEntity<Void> result = routeController.delete(7L);

    assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
  }

  private RouteResponse routeResponse(Long id, Boolean active) {
    return RouteResponse.builder()
        .id(id)
        .originTerminalId(10L)
        .destinationTerminalId(20L)
        .distanceKm(new BigDecimal("12.50"))
        .estimatedMinutes(30)
        .active(active)
        .build();
  }
}
