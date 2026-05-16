package cl.duoc.airflytrip.payments.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePaymentStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}
