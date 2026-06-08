package cl.duoc.airflytrip.routes.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

@ExtendWith(MockitoExtension.class)
class RouteControllerTest {

    @Mock
    private RouteService routeService;

    @InjectMocks
    private RouteController routeController;

    @Test
    void findAllShouldReturnMappedRoutes() {
        when(routeService.findAll()).thenReturn(List.of(routeResponse(1L, true)));

        var result = routeController.findAll();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getId()).isEqualTo(1L);
        verify(routeService).findAll();
    }

    @Test
    void findActiveShouldReturnMappedRoutes() {
        when(routeService.findActive()).thenReturn(List.of(routeResponse(2L, true)));

        var result = routeController.findActive();

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).getId()).isEqualTo(2L);
        verify(routeService).findActive();
    }

    @Test
    void createShouldReturnCreatedRoute() {
        CreateRouteRequest request = createRouteRequest();
        when(routeService.create(any(CreateRouteRequest.class))).thenReturn(routeResponse(10L, true));

        var result = routeController.create(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(10L);
        verify(routeService).create(any(CreateRouteRequest.class));
    }

    @Test
    void updateShouldReturnUpdatedRoute() {
        UpdateRouteRequest request = updateRouteRequest();
        when(routeService.update(5L, request)).thenReturn(routeResponse(5L, false));

        var result = routeController.update(5L, request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getActive()).isFalse();
        verify(routeService).update(5L, request);
    }

    @Test
    void deleteShouldReturnNoContent() {
        var result = routeController.delete(7L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(routeService).delete(7L);
    }

    private CreateRouteRequest createRouteRequest() {
        CreateRouteRequest request = new CreateRouteRequest();
        request.setOriginTerminalId(10L);
        request.setDestinationTerminalId(20L);
        request.setDistanceKm(new BigDecimal("12.50"));
        request.setEstimatedMinutes(30);
        request.setActive(true);
        return request;
    }

    private UpdateRouteRequest updateRouteRequest() {
        UpdateRouteRequest request = new UpdateRouteRequest();
        request.setOriginTerminalId(11L);
        request.setDestinationTerminalId(21L);
        request.setDistanceKm(new BigDecimal("14.25"));
        request.setEstimatedMinutes(35);
        request.setActive(false);
        return request;
    }

    private RouteResponse routeResponse(Long id, boolean active) {
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
