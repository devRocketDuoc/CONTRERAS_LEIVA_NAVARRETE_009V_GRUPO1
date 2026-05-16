package cl.duoc.airflytrip.chargingstations.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.duoc.airflytrip.chargingstations.clients.response.TerminalResponse;

@FeignClient(name = "terminal-service", url = "${services.terminal.url}")
public interface TerminalClient {

    @GetMapping("/api/v1/terminals/{id}")
    TerminalResponse findById(@PathVariable("id") Long id);
}
