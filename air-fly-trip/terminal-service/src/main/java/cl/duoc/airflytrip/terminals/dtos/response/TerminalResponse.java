package cl.duoc.airflytrip.terminals.dtos.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TerminalResponse {

    private Long id;

    private String name;

    private String city;

    private String locationDescription;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Boolean active;
}
