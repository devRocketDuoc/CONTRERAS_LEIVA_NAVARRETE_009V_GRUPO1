package cl.duoc.airflytrip.payments.clients;

import cl.duoc.airflytrip.payments.clients.response.TripResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "trip-service", url = "${services.trip.url}")
public interface TripClient {

    @GetMapping("/api/v1/trips/{id}")
    TripResponse findById(@PathVariable("id") Long id);
}
