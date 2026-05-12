package cl.duoc.airflytrip.terminals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TerminalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TerminalServiceApplication.class, args);
    }
}
