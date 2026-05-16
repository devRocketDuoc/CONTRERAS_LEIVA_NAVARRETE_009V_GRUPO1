package cl.duoc.airflytrip.payments.clients;

import cl.duoc.airflytrip.payments.clients.response.ReservationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservation-service", url = "${services.reservation.url}")
public interface ReservationClient {

    @GetMapping("/api/v1/reservations/{id}")
    ReservationResponse findById(@PathVariable("id") Long id);
}
