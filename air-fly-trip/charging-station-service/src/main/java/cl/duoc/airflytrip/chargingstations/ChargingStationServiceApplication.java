package cl.duoc.airflytrip.chargingstations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ChargingStationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChargingStationServiceApplication.class, args);
    }
}
