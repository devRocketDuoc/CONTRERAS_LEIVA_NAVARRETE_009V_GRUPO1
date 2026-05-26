package cl.duoc.airflytrip.vehicles.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVehicleStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}