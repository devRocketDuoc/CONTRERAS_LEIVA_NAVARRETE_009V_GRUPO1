package cl.duoc.airflytrip.trips.clients;

import cl.duoc.airflytrip.trips.clients.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {

    @GetMapping("/api/v1/auth/users/{id}")
    UserResponse findUserById(@PathVariable("id") Long id);
}
