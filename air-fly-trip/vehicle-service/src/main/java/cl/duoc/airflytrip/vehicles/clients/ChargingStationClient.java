package cl.duoc.airflytrip.vehicles.clients;

import cl.duoc.airflytrip.vehicles.clients.response.ChargingStationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "charging-station-service", url = "${services.charging-station.url}")
public interface ChargingStationClient {

    @GetMapping("/api/v1/charging-stations/{id}")
    ChargingStationResponse findById(@PathVariable("id") Long id);
}

