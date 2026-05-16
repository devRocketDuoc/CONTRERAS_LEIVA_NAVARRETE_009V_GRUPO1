package cl.duoc.airflytrip.tariffs.repositories;

import cl.duoc.airflytrip.tariffs.models.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TariffRepository extends JpaRepository<Tariff, Long> {

    List<Tariff> findByActiveTrue();

    List<Tariff> findByRouteId(Long routeId);

    Optional<Tariff> findByRouteIdAndVehicleTypeIgnoreCase(Long routeId, String vehicleType);

    boolean existsByRouteIdAndVehicleTypeIgnoreCase(Long routeId, String vehicleType);
}