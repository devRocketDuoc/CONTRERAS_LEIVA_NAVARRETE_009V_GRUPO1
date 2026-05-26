package cl.duoc.airflytrip.tariffs.clients;

import cl.duoc.airflytrip.tariffs.clients.response.RouteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "route-service", url = "${services.route.url}")
public interface RouteClient {

    @GetMapping("/api/v1/routes/{id}")
    RouteResponse findById(@PathVariable("id") Long id);
}