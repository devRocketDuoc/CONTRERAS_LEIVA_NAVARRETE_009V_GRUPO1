package cl.duoc.airflytrip.trips.services;

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
import cl.duoc.airflytrip.trips.exceptions.NotFoundException;
import cl.duoc.airflytrip.trips.exceptions.RemoteServiceException;
import cl.duoc.airflytrip.trips.models.Trip;
import cl.duoc.airflytrip.trips.repositories.TripRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private static final String REQUESTED_STATUS = "REQUESTED";
    private static final String IN_PROGRESS_STATUS = "IN_PROGRESS";
    private static final String FINISHED_STATUS = "FINISHED";
    private static final String CANCELLED_STATUS = "CANCELLED";

    private final TripRepository tripRepository;
    private final AuthClient authClient;
    private final TerminalClient terminalClient;
    private final VehicleClient vehicleClient;
    private final RouteClient routeClient;

    public List<TripResponse> findAll() {
        return tripRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<TripResponse> findActive() {
        return tripRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    public TripResponse findById(Long id) {
        return toResponse(findTripById(id));
    }

    public List<TripResponse> findByUserId(Long userId) {
        validateUserExists(userId);
        return tripRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public TripResponse create(CreateTripRequest request) {
        validateUserExists(request.getUserId());
        validateTerminalExists(request.getOriginTerminalId(), "origin");
        validateTerminalExists(request.getDestinationTerminalId(), "destination");
        RouteResponse route = validateRouteExists(request.getRouteId());
        validateRouteMatchesTerminals(route, request.getOriginTerminalId(), request.getDestinationTerminalId());
        validateVehicleIfPresent(request.getVehicleId());
        validateScheduledAt(request.getScheduledAt());

        Trip trip = Trip.builder()
                .userId(request.getUserId())
                .vehicleId(request.getVehicleId())
                .originTerminalId(request.getOriginTerminalId())
                .destinationTerminalId(request.getDestinationTerminalId())
                .routeId(request.getRouteId())
                .status(REQUESTED_STATUS)
                .scheduledAt(request.getScheduledAt())
                .startedAt(null)
                .finishedAt(null)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(tripRepository.save(trip));
    }

    public TripResponse updateStatus(Long id, UpdateTripStatusRequest request) {
        Trip trip = findTripById(id);
        validateTripCanChangeStatus(trip);

        trip.setStatus(request.getStatus());

        if (CANCELLED_STATUS.equalsIgnoreCase(request.getStatus())) {
            trip.setActive(false);
        }

        return toResponse(tripRepository.save(trip));
    }

    public TripResponse startTrip(Long id) {
        Trip trip = findTripById(id);

        if (FINISHED_STATUS.equalsIgnoreCase(trip.getStatus()) || CANCELLED_STATUS.equalsIgnoreCase(trip.getStatus())) {
            throw new BadRequestException("Finished or cancelled trips cannot be started");
        }

        trip.setStatus(IN_PROGRESS_STATUS);
        trip.setStartedAt(LocalDateTime.now());

        return toResponse(tripRepository.save(trip));
    }

    public TripResponse finishTrip(Long id) {
        Trip trip = findTripById(id);

        if (!IN_PROGRESS_STATUS.equalsIgnoreCase(trip.getStatus())) {
            throw new BadRequestException("Only trips in progress can be finished");
        }

        trip.setStatus(FINISHED_STATUS);
        trip.setFinishedAt(LocalDateTime.now());
        trip.setActive(false);

        return toResponse(tripRepository.save(trip));
    }

    public void delete(Long id) {
        Trip trip = findTripById(id);
        trip.setStatus(CANCELLED_STATUS);
        trip.setActive(false);
        tripRepository.save(trip);
    }

    private Trip findTripById(Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Trip not found with id: " + id));
    }

    private void validateUserExists(Long userId) {
        try {
            UserResponse user = authClient.findUserById(userId);

            if (user == null || Boolean.FALSE.equals(user.getEnabled())) {
                throw new BadRequestException("User is not enabled or does not exist with id: " + userId);
            }
        } catch (FeignException.NotFound exception) {
            throw new BadRequestException("User not found with id: " + userId);
        } catch (FeignException exception) {
            throw new RemoteServiceException("Error communicating with auth-service");
        }
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

    private RouteResponse validateRouteExists(Long routeId) {
        try {
            RouteResponse route = routeClient.findById(routeId);

            if (route == null || Boolean.FALSE.equals(route.getActive())) {
                throw new BadRequestException("Route is not active or does not exist with id: " + routeId);
            }

            return route;
        } catch (FeignException.NotFound exception) {
            throw new BadRequestException("Route not found with id: " + routeId);
        } catch (FeignException exception) {
            throw new RemoteServiceException("Error communicating with route-service");
        }
    }

    private void validateVehicleIfPresent(Long vehicleId) {
        if (vehicleId == null) {
            return;
        }

        try {
            VehicleResponse vehicle = vehicleClient.findById(vehicleId);

            if (vehicle == null || Boolean.FALSE.equals(vehicle.getActive())) {
                throw new BadRequestException("Vehicle is not active or does not exist with id: " + vehicleId);
            }
        } catch (FeignException.NotFound exception) {
            throw new BadRequestException("Vehicle not found with id: " + vehicleId);
        } catch (FeignException exception) {
            throw new RemoteServiceException("Error communicating with vehicle-service");
        }
    }

    private void validateRouteMatchesTerminals(RouteResponse route, Long originTerminalId, Long destinationTerminalId) {
        if (!route.getOriginTerminalId().equals(originTerminalId)
                || !route.getDestinationTerminalId().equals(destinationTerminalId)) {
            throw new BadRequestException("Route does not match origin and destination terminals");
        }
    }

    private void validateScheduledAt(LocalDateTime scheduledAt) {
        if (scheduledAt == null || scheduledAt.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Scheduled date must be now or in the future");
        }
    }

    private void validateTripCanChangeStatus(Trip trip) {
        if (FINISHED_STATUS.equalsIgnoreCase(trip.getStatus()) || CANCELLED_STATUS.equalsIgnoreCase(trip.getStatus())) {
            throw new BadRequestException("Finished or cancelled trips cannot change status");
        }
    }

    private TripResponse toResponse(Trip trip) {
        return TripResponse.builder()
                .id(trip.getId())
                .userId(trip.getUserId())
                .vehicleId(trip.getVehicleId())
                .originTerminalId(trip.getOriginTerminalId())
                .destinationTerminalId(trip.getDestinationTerminalId())
                .routeId(trip.getRouteId())
                .status(trip.getStatus())
                .scheduledAt(trip.getScheduledAt())
                .startedAt(trip.getStartedAt())
                .finishedAt(trip.getFinishedAt())
                .active(trip.getActive())
                .createdAt(trip.getCreatedAt())
                .build();
    }
}
