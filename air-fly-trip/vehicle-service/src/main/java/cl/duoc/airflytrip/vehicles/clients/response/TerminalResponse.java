package cl.duoc.airflytrip.vehicles.clients.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TerminalResponse {

    private Long id;
    private String name;
    private String city;
    private String locationDescription;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean active;
}
