package cl.duoc.airflytrip.routes.repositories;

import cl.duoc.airflytrip.routes.models.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    List<Route> findByActiveTrue();

    Optional<Route> findByOriginTerminalIdAndDestinationTerminalId(Long originTerminalId, Long destinationTerminalId);

    boolean existsByOriginTerminalIdAndDestinationTerminalId(Long originTerminalId, Long destinationTerminalId);
}
