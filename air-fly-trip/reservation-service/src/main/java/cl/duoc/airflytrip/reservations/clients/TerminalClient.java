package cl.duoc.airflytrip.reservations.clients;

import cl.duoc.airflytrip.reservations.clients.response.TerminalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "terminal-service", url = "${services.terminal.url}")
public interface TerminalClient {

    @GetMapping("/api/v1/terminals/{id}")
    TerminalResponse findById(@PathVariable("id") Long id);
}