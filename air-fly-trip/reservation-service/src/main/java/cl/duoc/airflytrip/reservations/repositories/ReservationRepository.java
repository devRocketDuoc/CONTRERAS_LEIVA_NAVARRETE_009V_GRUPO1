package cl.duoc.airflytrip.reservations.repositories;

import cl.duoc.airflytrip.reservations.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByActiveTrue();

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByStatusIgnoreCase(String status);
}