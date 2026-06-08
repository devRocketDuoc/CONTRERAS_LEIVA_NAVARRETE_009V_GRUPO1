package cl.duoc.airflytrip.routes.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import cl.duoc.airflytrip.routes.clients.TerminalClient;
import cl.duoc.airflytrip.routes.clients.response.TerminalResponse;
import cl.duoc.airflytrip.routes.dtos.request.CreateRouteRequest;
import cl.duoc.airflytrip.routes.dtos.request.UpdateRouteRequest;
import cl.duoc.airflytrip.routes.dtos.response.RouteResponse;
import cl.duoc.airflytrip.routes.exceptions.BadRequestException;
import cl.duoc.airflytrip.routes.exceptions.NotFoundException;
import cl.duoc.airflytrip.routes.models.Route;
import cl.duoc.airflytrip.routes.repositories.RouteRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private TerminalClient terminalClient;

    @InjectMocks
    private RouteService routeService;

    @Test
    void findAllShouldMapEveryRoute() {
        when(routeRepository.findAll()).thenReturn(List.of(
                route(1L, 10L, 20L, "12.50", 30, true),
                route(2L, 20L, 30L, "18.75", 45, false)
        ));

        List<RouteResponse> result = routeService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getActive()).isFalse();
    }

    @Test
    void findActiveShouldUseActiveOnlyQuery() {
        when(routeRepository.findByActiveTrue()).thenReturn(List.of(route(1L, 10L, 20L, "12.50", 30, true)));

        List<RouteResponse> result = routeService.findActive();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(routeRepository).findByActiveTrue();
    }

    @Test
    void findByIdShouldReturnRouteWhenPresent() {
        when(routeRepository.findById(7L)).thenReturn(Optional.of(route(7L, 10L, 20L, "8.40", 20, true)));

        RouteResponse result = routeService.findById(7L);

        assertThat(result.getId()).isEqualTo(7L);
        assertThat(result.getOriginTerminalId()).isEqualTo(10L);
    }

    @Test
    void findByIdShouldThrowWhenMissing() {
        when(routeRepository.findById(7L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> routeService.findById(7L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Route not found with id: 7");
    }

    @Test
    void findByOriginAndDestinationShouldReturnMatchingRoute() {
        when(routeRepository.findByOriginTerminalIdAndDestinationTerminalId(10L, 20L))
                .thenReturn(Optional.of(route(9L, 10L, 20L, "10.00", 25, true)));

        RouteResponse result = routeService.findByOriginAndDestination(10L, 20L);

        assertThat(result.getId()).isEqualTo(9L);
        assertThat(result.getDestinationTerminalId()).isEqualTo(20L);
    }

    @Test
    void createShouldPersistValidatedRoute() {
        CreateRouteRequest request = new CreateRouteRequest();
        request.setOriginTerminalId(10L);
        request.setDestinationTerminalId(20L);
        request.setDistanceKm(new BigDecimal("15.75"));
        request.setEstimatedMinutes(35);

        when(terminalClient.findById(10L)).thenReturn(terminal(10L, true));
        when(terminalClient.findById(20L)).thenReturn(terminal(20L, true));
        when(routeRepository.existsByOriginTerminalIdAndDestinationTerminalId(10L, 20L)).thenReturn(false);
        when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> {
            Route route = invocation.getArgument(0);
            route.setId(100L);
            return route;
        });

        RouteResponse result = routeService.create(request);

        ArgumentCaptor<Route> captor = ArgumentCaptor.forClass(Route.class);
        verify(routeRepository).save(captor.capture());

        Route saved = captor.getValue();
        assertThat(saved.getOriginTerminalId()).isEqualTo(10L);
        assertThat(saved.getDestinationTerminalId()).isEqualTo(20L);
        assertThat(saved.getActive()).isTrue();
        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getDistanceKm()).isEqualByComparingTo("15.75");
    }

    @Test
    void createShouldRejectSameOriginAndDestination() {
        CreateRouteRequest request = new CreateRouteRequest();
        request.setOriginTerminalId(10L);
        request.setDestinationTerminalId(10L);
        request.setDistanceKm(new BigDecimal("15.75"));
        request.setEstimatedMinutes(35);

        assertThatThrownBy(() -> routeService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Origin and destination terminals cannot be the same");

        verifyNoInteractions(routeRepository, terminalClient);
    }

    @Test
    void createShouldRejectWhenTerminalIsInactive() {
        CreateRouteRequest request = new CreateRouteRequest();
        request.setOriginTerminalId(10L);
        request.setDestinationTerminalId(20L);
        request.setDistanceKm(new BigDecimal("15.75"));
        request.setEstimatedMinutes(35);

        when(terminalClient.findById(10L)).thenReturn(terminal(10L, false));

        assertThatThrownBy(() -> routeService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("origin terminal is not active or does not exist with id: 10");

        verify(routeRepository, never()).save(any());
    }

    @Test
    void createShouldRejectDuplicateRoute() {
        CreateRouteRequest request = new CreateRouteRequest();
        request.setOriginTerminalId(10L);
        request.setDestinationTerminalId(20L);
        request.setDistanceKm(new BigDecimal("15.75"));
        request.setEstimatedMinutes(35);

        when(terminalClient.findById(10L)).thenReturn(terminal(10L, true));
        when(terminalClient.findById(20L)).thenReturn(terminal(20L, true));
        when(routeRepository.existsByOriginTerminalIdAndDestinationTerminalId(10L, 20L)).thenReturn(true);

        assertThatThrownBy(() -> routeService.create(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Route already exists from terminal 10 to terminal 20");

        verify(routeRepository, never()).save(any());
    }

    @Test
    void updateShouldPreserveActiveFlagWhenNotProvided() {
        UpdateRouteRequest request = new UpdateRouteRequest();
        request.setOriginTerminalId(11L);
        request.setDestinationTerminalId(21L);
        request.setDistanceKm(new BigDecimal("18.40"));
        request.setEstimatedMinutes(40);

        Route existing = route(50L, 10L, 20L, "10.00", 25, true);
        when(routeRepository.findById(50L)).thenReturn(Optional.of(existing));
        when(terminalClient.findById(11L)).thenReturn(terminal(11L, true));
        when(terminalClient.findById(21L)).thenReturn(terminal(21L, true));
        when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RouteResponse result = routeService.update(50L, request);

        assertThat(result.getId()).isEqualTo(50L);
        assertThat(result.getActive()).isTrue();
        assertThat(result.getOriginTerminalId()).isEqualTo(11L);
        assertThat(existing.getDestinationTerminalId()).isEqualTo(21L);
    }

    @Test
    void deleteShouldDeactivateRoute() {
        Route existing = route(60L, 10L, 20L, "10.00", 25, true);
        when(routeRepository.findById(60L)).thenReturn(Optional.of(existing));
        when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));

        routeService.delete(60L);

        ArgumentCaptor<Route> captor = ArgumentCaptor.forClass(Route.class);
        verify(routeRepository).save(captor.capture());
        assertThat(captor.getValue().getActive()).isFalse();
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

    private TerminalResponse terminal(Long id, Boolean active) {
        TerminalResponse response = new TerminalResponse();
        response.setId(id);
        response.setName("Terminal " + id);
        response.setCity("Santiago");
        response.setLocationDescription("Location " + id);
        response.setActive(active);
        return response;
    }
}
