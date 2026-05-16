package cl.duoc.airflytrip.terminals.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.duoc.airflytrip.terminals.models.Terminal;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {

    List<Terminal> findByActiveTrue();

    boolean existsByNameIgnoreCase(String name);
}
