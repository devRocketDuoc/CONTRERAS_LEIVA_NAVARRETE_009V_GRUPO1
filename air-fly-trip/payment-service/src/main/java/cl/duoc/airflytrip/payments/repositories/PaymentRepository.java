package cl.duoc.airflytrip.payments.repositories;

import cl.duoc.airflytrip.payments.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByTripId(Long tripId);

    List<Payment> findByReservationId(Long reservationId);

    List<Payment> findByStatusIgnoreCase(String status);
}
