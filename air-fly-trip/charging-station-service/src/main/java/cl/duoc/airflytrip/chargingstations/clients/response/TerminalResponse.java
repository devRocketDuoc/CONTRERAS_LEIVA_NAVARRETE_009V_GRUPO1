package cl.duoc.airflytrip.chargingstations.clients.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

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
