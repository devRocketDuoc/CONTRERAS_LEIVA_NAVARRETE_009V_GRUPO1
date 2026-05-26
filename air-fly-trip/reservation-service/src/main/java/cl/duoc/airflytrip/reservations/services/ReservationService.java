package cl.duoc.airflytrip.reservations.services;

import cl.duoc.airflytrip.reservations.clients.AuthClient;
import cl.duoc.airflytrip.reservations.clients.RouteClient;
import cl.duoc.airflytrip.reservations.clients.TerminalClient;
import cl.duoc.airflytrip.reservations.clients.response.RouteResponse;
import cl.duoc.airflytrip.reservations.clients.response.TerminalResponse;
import cl.duoc.airflytrip.reservations.clients.response.UserResponse;
import cl.duoc.airflytrip.reservations.dtos.request.CreateReservationRequest;
import cl.duoc.airflytrip.reservations.dtos.request.UpdateReservationStatusRequest;
import cl.duoc.airflytrip.reservations.dtos.response.ReservationResponse;
import cl.duoc.airflytrip.reservations.exceptions.BadRequestException;
import cl.duoc.airflytrip.reservations.exceptions.NotFoundException;
import cl.duoc.airflytrip.reservations.exceptions.RemoteServiceException;
import cl.duoc.airflytrip.reservations.models.Reservation;
import cl.duoc.airflytrip.reservations.repositories.ReservationRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final String DEFAULT_STATUS = "PENDING";
    private static final String CANCELLED_STATUS = "CANCELLED";

    private final ReservationRepository reservationRepository;
    private final AuthClient authClient;
    private final TerminalClient terminalClient;
    private final RouteClient routeClient;

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<ReservationResponse> findActive() {
        return reservationRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    public ReservationResponse findById(Long id) {
        return toResponse(findReservationById(id));
    }

    public List<ReservationResponse> findByUserId(Long userId) {
        validateUserExists(userId);
        return reservationRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public ReservationResponse create(CreateReservationRequest request) {
        validateUserExists(request.getUserId());
        validateTerminalExists(request.getOriginTerminalId(), "origin");
        validateTerminalExists(request.getDestinationTerminalId(), "destination");
        RouteResponse route = validateRouteExists(request.getRouteId());
        validateRouteMatchesTerminals(route, request.getOriginTerminalId(), request.getDestinationTerminalId());
        validateReservedAt(request.getReservedAt());

        Reservation reservation = Reservation.builder()
                .userId(request.getUserId())
                .routeId(request.getRouteId())
                .originTerminalId(request.getOriginTerminalId())
                .destinationTerminalId(request.getDestinationTerminalId())
                .reservedAt(request.getReservedAt())
                .status(DEFAULT_STATUS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(reservationRepository.save(reservation));
    }

    public ReservationResponse updateStatus(Long id, UpdateReservationStatusRequest request) {
        Reservation reservation = findReservationById(id);

        if (CANCELLED_STATUS.equalsIgnoreCase(reservation.getStatus())) {
            throw new BadRequestException("Cancelled reservations cannot change status");
        }

        reservation.setStatus(request.getStatus());

        if (CANCELLED_STATUS.equalsIgnoreCase(request.getStatus())) {
            reservation.setActive(false);
        }

        return toResponse(reservationRepository.save(reservation));
    }

    public void delete(Long id) {
        Reservation reservation = findReservationById(id);
        reservation.setStatus(CANCELLED_STATUS);
        reservation.setActive(false);
        reservationRepository.save(reservation);
    }

    private Reservation findReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Reservation not found with id: " + id));
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

    private void validateRouteMatchesTerminals(RouteResponse route, Long originTerminalId, Long destinationTerminalId) {
        if (!route.getOriginTerminalId().equals(originTerminalId)
                || !route.getDestinationTerminalId().equals(destinationTerminalId)) {
            throw new BadRequestException("Route does not match origin and destination terminals");
        }
    }

    private void validateReservedAt(LocalDateTime reservedAt) {
        if (reservedAt == null || !reservedAt.isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Reserved date must be in the future");
        }
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .userId(reservation.getUserId())
                .routeId(reservation.getRouteId())
                .originTerminalId(reservation.getOriginTerminalId())
                .destinationTerminalId(reservation.getDestinationTerminalId())
                .reservedAt(reservation.getReservedAt())
                .status(reservation.getStatus())
                .active(reservation.getActive())
                .createdAt(reservation.getCreatedAt())
                .build();
    }
}