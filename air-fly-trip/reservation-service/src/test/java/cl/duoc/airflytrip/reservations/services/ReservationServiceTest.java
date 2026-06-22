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
import cl.duoc.airflytrip.reservations.models.Reservation;
import cl.duoc.airflytrip.reservations.repositories.ReservationRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AuthClient authClient;

    @Mock
    private TerminalClient terminalClient;

    @Mock
    private RouteClient routeClient;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void createShouldPersistReservationSuccessfully() {

        CreateReservationRequest request = new CreateReservationRequest();
        request.setUserId(12L);
        request.setRouteId(8L);
        request.setOriginTerminalId(3L);
        request.setDestinationTerminalId(5L);
        request.setReservedAt(LocalDateTime.now().plusDays(2));

        when(authClient.findUserById(12L)).thenReturn(enabledUser(12L));
        when(terminalClient.findById(3L)).thenReturn(activeTerminal(3L));
        when(terminalClient.findById(5L)).thenReturn(activeTerminal(5L));
        when(routeClient.findById(8L)).thenReturn(activeRoute(8L, 3L, 5L));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation reservation = invocation.getArgument(0);
            reservation.setId(40L);
            return reservation;
        });

        ReservationResponse response = reservationService.create(request);

        assertNotNull(response);
        assertEquals(40L, response.getId());
        assertEquals("PENDING", response.getStatus());
        assertEquals(true, response.getActive());
        assertEquals(12L, response.getUserId());
    }

    @Test
    void createShouldFailWhenRouteDoesNotMatchTerminals() {

        CreateReservationRequest request = new CreateReservationRequest();
        request.setUserId(12L);
        request.setRouteId(8L);
        request.setOriginTerminalId(3L);
        request.setDestinationTerminalId(5L);
        request.setReservedAt(LocalDateTime.now().plusDays(2));

        when(authClient.findUserById(12L)).thenReturn(enabledUser(12L));
        when(terminalClient.findById(3L)).thenReturn(activeTerminal(3L));
        when(terminalClient.findById(5L)).thenReturn(activeTerminal(5L));
        when(routeClient.findById(8L)).thenReturn(activeRoute(8L, 3L, 9L));

        Exception exception = null;

        try {
            reservationService.create(request);
        } catch (Exception ex) {
            exception = ex;
        }

        assertNotNull(exception);
        assertEquals(BadRequestException.class, exception.getClass());
        assertEquals("Route does not match origin and destination terminals", exception.getMessage());
    }

    @Test
    void updateStatusShouldFailWhenReservationIsCancelled() {

        UpdateReservationStatusRequest request = new UpdateReservationStatusRequest();
        request.setStatus("CONFIRMED");

        when(reservationRepository.findById(40L)).thenReturn(Optional.of(sampleReservation(40L, "CANCELLED", false)));

        Exception exception = null;

        try {
            reservationService.updateStatus(40L, request);
        } catch (Exception ex) {
            exception = ex;
        }

        assertNotNull(exception);
        assertEquals(BadRequestException.class, exception.getClass());
        assertEquals("Cancelled reservations cannot change status", exception.getMessage());
    }

    @Test
    void deleteShouldCancelReservation() {

        Reservation reservation = sampleReservation(40L, "PENDING", true);

        when(reservationRepository.findById(40L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        reservationService.delete(40L);

        assertEquals("CANCELLED", reservation.getStatus());
        assertFalse(reservation.getActive());
        verify(reservationRepository).save(reservation);
    }

    private UserResponse enabledUser(Long id) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(id);
        userResponse.setEmail("user@example.com");
        userResponse.setEnabled(true);
        userResponse.setCreatedAt(LocalDateTime.of(2026, 1, 1, 8, 0));
        return userResponse;
    }

    private TerminalResponse activeTerminal(Long id) {
        TerminalResponse terminalResponse = new TerminalResponse();
        terminalResponse.setId(id);
        terminalResponse.setName("Terminal " + id);
        terminalResponse.setCity("Santiago");
        terminalResponse.setActive(true);
        return terminalResponse;
    }

    private RouteResponse activeRoute(Long id, Long originTerminalId, Long destinationTerminalId) {
        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setId(id);
        routeResponse.setOriginTerminalId(originTerminalId);
        routeResponse.setDestinationTerminalId(destinationTerminalId);
        routeResponse.setActive(true);
        return routeResponse;
    }

    private Reservation sampleReservation(Long id, String status, Boolean active) {
        return Reservation.builder()
                .id(id)
                .userId(12L)
                .routeId(8L)
                .originTerminalId(3L)
                .destinationTerminalId(5L)
                .reservedAt(LocalDateTime.now().plusDays(1))
                .status(status)
                .active(active)
                .createdAt(LocalDateTime.of(2026, 6, 21, 9, 0))
                .build();
    }
}
