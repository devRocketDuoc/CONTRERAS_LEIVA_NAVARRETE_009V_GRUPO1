package cl.duoc.airflytrip.routes.services;

import cl.duoc.airflytrip.routes.clients.TerminalClient;
import cl.duoc.airflytrip.routes.clients.response.TerminalResponse;
import cl.duoc.airflytrip.routes.dtos.request.CreateRouteRequest;
import cl.duoc.airflytrip.routes.dtos.request.UpdateRouteRequest;
import cl.duoc.airflytrip.routes.dtos.response.RouteResponse;
import cl.duoc.airflytrip.routes.exceptions.BadRequestException;
import cl.duoc.airflytrip.routes.exceptions.NotFoundException;
import cl.duoc.airflytrip.routes.exceptions.RemoteServiceException;
import cl.duoc.airflytrip.routes.models.Route;
import cl.duoc.airflytrip.routes.repositories.RouteRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final TerminalClient terminalClient;

    public List<RouteResponse> findAll() {
        return routeRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<RouteResponse> findActive() {
        return routeRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    public RouteResponse findById(Long id) {
        return toResponse(findRouteById(id));
    }

    public RouteResponse findByOriginAndDestination(Long originTerminalId, Long destinationTerminalId) {
        Route route = routeRepository.findByOriginTerminalIdAndDestinationTerminalId(originTerminalId, destinationTerminalId)
                .orElseThrow(() -> new NotFoundException(
                        "Route not found from terminal " + originTerminalId + " to terminal " + destinationTerminalId
                ));

        return toResponse(route);
    }

    public RouteResponse create(CreateRouteRequest request) {
        validateTerminals(request.getOriginTerminalId(), request.getDestinationTerminalId());
        validateDistanceAndTime(request.getDistanceKm(), request.getEstimatedMinutes());
        validateRouteDoesNotExist(request.getOriginTerminalId(), request.getDestinationTerminalId());

        Route route = Route.builder()
                .originTerminalId(request.getOriginTerminalId())
                .destinationTerminalId(request.getDestinationTerminalId())
                .distanceKm(request.getDistanceKm())
                .estimatedMinutes(request.getEstimatedMinutes())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        return toResponse(routeRepository.save(route));
    }

    public RouteResponse update(Long id, UpdateRouteRequest request) {
        Route route = findRouteById(id);

        validateTerminals(request.getOriginTerminalId(), request.getDestinationTerminalId());
        validateDistanceAndTime(request.getDistanceKm(), request.getEstimatedMinutes());

        route.setOriginTerminalId(request.getOriginTerminalId());
        route.setDestinationTerminalId(request.getDestinationTerminalId());
        route.setDistanceKm(request.getDistanceKm());
        route.setEstimatedMinutes(request.getEstimatedMinutes());
        route.setActive(request.getActive() != null ? request.getActive() : route.getActive());

        return toResponse(routeRepository.save(route));
    }

    public void delete(Long id) {
        Route route = findRouteById(id);
        route.setActive(false);
        routeRepository.save(route);
    }

    private Route findRouteById(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Route not found with id: " + id));
    }

    private void validateTerminals(Long originTerminalId, Long destinationTerminalId) {
        if (originTerminalId.equals(destinationTerminalId)) {
            throw new BadRequestException("Origin and destination terminals cannot be the same");
        }

        validateTerminalExists(originTerminalId, "origin");
        validateTerminalExists(destinationTerminalId, "destination");
    }

    private void validateTerminalExists(Long terminalId, String type) {
        try {
            TerminalResponse terminal = terminalClient.findById(terminalId);

            if (terminal == null || Boolean.FALSE.equals(terminal.getActive())) {
                throw new BadRequestException(type + " terminal is not active or does not exist with id: " + terminalId);
            }
        } catch (FeignException.NotFound exception) {
            throw new BadRequestException(type + " terminal not found with id: " + terminalId);
        } catch (FeignException exception) {
            throw new RemoteServiceException("Error communicating with terminal-service");
        }
    }

    private void validateDistanceAndTime(java.math.BigDecimal distanceKm, Integer estimatedMinutes) {
        if (distanceKm == null || distanceKm.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Distance must be greater than 0");
        }

        if (estimatedMinutes == null || estimatedMinutes <= 0) {
            throw new BadRequestException("Estimated minutes must be greater than 0");
        }
    }

    private void validateRouteDoesNotExist(Long originTerminalId, Long destinationTerminalId) {
        if (routeRepository.existsByOriginTerminalIdAndDestinationTerminalId(originTerminalId, destinationTerminalId)) {
            throw new BadRequestException("Route already exists from terminal " + originTerminalId + " to terminal " + destinationTerminalId);
        }
    }

    private RouteResponse toResponse(Route route) {
        return RouteResponse.builder()
                .id(route.getId())
                .originTerminalId(route.getOriginTerminalId())
                .destinationTerminalId(route.getDestinationTerminalId())
                .distanceKm(route.getDistanceKm())
                .estimatedMinutes(route.getEstimatedMinutes())
                .active(route.getActive())
                .build();
    }
}
