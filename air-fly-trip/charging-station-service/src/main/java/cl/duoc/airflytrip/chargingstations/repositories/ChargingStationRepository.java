package cl.duoc.airflytrip.chargingstations.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.airflytrip.chargingstations.models.ChargingStation;

public interface ChargingStationRepository extends JpaRepository<ChargingStation, Long> {

    List<ChargingStation> findByActiveTrue();

    List<ChargingStation> findByTerminalId(Long terminalId);

    boolean existsByCodeIgnoreCase(String code);
}
