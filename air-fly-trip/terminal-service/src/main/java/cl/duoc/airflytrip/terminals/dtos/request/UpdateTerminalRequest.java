package cl.duoc.airflytrip.terminals.dtos.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTerminalRequest {

    @NotBlank(message = "Terminal name is required")
    private String name;

    @NotBlank(message = "Terminal city is required")
    private String city;

    private String locationDescription;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Boolean active;
}
