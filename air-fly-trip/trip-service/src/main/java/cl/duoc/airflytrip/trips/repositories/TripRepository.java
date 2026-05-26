package cl.duoc.airflytrip.trips.repositories;

import cl.duoc.airflytrip.trips.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByActiveTrue();

    List<Trip> findByUserId(Long userId);

    List<Trip> findByStatusIgnoreCase(String status);
}
