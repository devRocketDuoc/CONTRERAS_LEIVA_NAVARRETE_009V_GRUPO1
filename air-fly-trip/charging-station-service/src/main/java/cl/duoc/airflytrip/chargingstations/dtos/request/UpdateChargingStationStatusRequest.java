package cl.duoc.airflytrip.chargingstations.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChargingStationStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}
