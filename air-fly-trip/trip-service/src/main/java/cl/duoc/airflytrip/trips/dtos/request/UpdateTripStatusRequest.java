package cl.duoc.airflytrip.trips.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTripStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}
