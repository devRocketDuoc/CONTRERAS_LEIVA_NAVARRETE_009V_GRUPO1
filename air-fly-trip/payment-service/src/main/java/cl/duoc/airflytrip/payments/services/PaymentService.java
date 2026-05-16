package cl.duoc.airflytrip.payments.services;

import cl.duoc.airflytrip.payments.clients.ReservationClient;
import cl.duoc.airflytrip.payments.clients.TripClient;
import cl.duoc.airflytrip.payments.clients.response.ReservationResponse;
import cl.duoc.airflytrip.payments.clients.response.TripResponse;
import cl.duoc.airflytrip.payments.dtos.request.CreatePaymentRequest;
import cl.duoc.airflytrip.payments.dtos.request.UpdatePaymentStatusRequest;
import cl.duoc.airflytrip.payments.dtos.response.PaymentResponse;
import cl.duoc.airflytrip.payments.exceptions.BadRequestException;
import cl.duoc.airflytrip.payments.exceptions.NotFoundException;
import cl.duoc.airflytrip.payments.exceptions.RemoteServiceException;
import cl.duoc.airflytrip.payments.models.Payment;
import cl.duoc.airflytrip.payments.repositories.PaymentRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final String PENDING_STATUS = "PENDING";
    private static final String APPROVED_STATUS = "APPROVED";

    private final PaymentRepository paymentRepository;
    private final TripClient tripClient;
    private final ReservationClient reservationClient;

    public List<PaymentResponse> findAll() {
        return paymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    public PaymentResponse findById(Long id) {
        return toResponse(findPaymentById(id));
    }

    public List<PaymentResponse> findByTripId(Long tripId) {
        validateTripExists(tripId);
        return paymentRepository.findByTripId(tripId).stream().map(this::toResponse).toList();
    }

    public List<PaymentResponse> findByReservationId(Long reservationId) {
        validateReservationExists(reservationId);
        return paymentRepository.findByReservationId(reservationId).stream().map(this::toResponse).toList();
    }

    public PaymentResponse create(CreatePaymentRequest request) {
        validatePaymentReference(request.getTripId(), request.getReservationId());
        validateAmount(request.getAmount());

        if (request.getTripId() != null) {
            validateTripExists(request.getTripId());
        }

        if (request.getReservationId() != null) {
            validateReservationExists(request.getReservationId());
        }

        String status = request.getStatus() != null ? request.getStatus() : PENDING_STATUS;

        Payment payment = Payment.builder()
                .tripId(request.getTripId())
                .reservationId(request.getReservationId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(status)
                .paidAt(APPROVED_STATUS.equalsIgnoreCase(status) ? LocalDateTime.now() : null)
                .createdAt(LocalDateTime.now())
                .build();

        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse updateStatus(Long id, UpdatePaymentStatusRequest request) {
        Payment payment = findPaymentById(id);

        if (APPROVED_STATUS.equalsIgnoreCase(payment.getStatus())) {
            throw new BadRequestException("Approved payments cannot change status");
        }

        payment.setStatus(request.getStatus());

        if (APPROVED_STATUS.equalsIgnoreCase(request.getStatus())) {
            payment.setPaidAt(LocalDateTime.now());
        }

        return toResponse(paymentRepository.save(payment));
    }

    private Payment findPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found with id: " + id));
    }

    private void validatePaymentReference(Long tripId, Long reservationId) {
        if (tripId == null && reservationId == null) {
            throw new BadRequestException("Payment must be associated with a trip or a reservation");
        }

        if (tripId != null && reservationId != null) {
            throw new BadRequestException("Payment cannot be associated with both trip and reservation at the same time");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Amount must be greater than 0");
        }
    }

    private void validateTripExists(Long tripId) {
        try {
            TripResponse trip = tripClient.findById(tripId);

            if (trip == null) {
                throw new BadRequestException("Trip does not exist with id: " + tripId);
            }
        } catch (FeignException.NotFound exception) {
            throw new BadRequestException("Trip not found with id: " + tripId);
        } catch (FeignException exception) {
            throw new RemoteServiceException("Error communicating with trip-service");
        }
    }

    private void validateReservationExists(Long reservationId) {
        try {
            ReservationResponse reservation = reservationClient.findById(reservationId);

            if (reservation == null) {
                throw new BadRequestException("Reservation does not exist with id: " + reservationId);
            }
        } catch (FeignException.NotFound exception) {
            throw new BadRequestException("Reservation not found with id: " + reservationId);
        } catch (FeignException exception) {
            throw new RemoteServiceException("Error communicating with reservation-service");
        }
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .tripId(payment.getTripId())
                .reservationId(payment.getReservationId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
