package cl.duoc.airflytrip.vehicles.repositories;

import cl.duoc.airflytrip.vehicles.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByActiveTrue();

    List<Vehicle> findByStatusIgnoreCase(String status);

    List<Vehicle> findByTerminalId(Long terminalId);

    boolean existsByCodeIgnoreCase(String code);
}
