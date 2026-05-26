package cl.duoc.airflytrip.trips.clients;

import cl.duoc.airflytrip.trips.clients.response.VehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vehicle-service", url = "${services.vehicle.url}")
public interface VehicleClient {

    @GetMapping("/api/v1/vehicles/{id}")
    VehicleResponse findById(@PathVariable("id") Long id);
}
