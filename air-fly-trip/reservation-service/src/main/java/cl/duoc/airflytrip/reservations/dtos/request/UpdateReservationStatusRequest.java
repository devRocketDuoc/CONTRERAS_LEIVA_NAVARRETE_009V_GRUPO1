package cl.duoc.airflytrip.reservations.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReservationStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}