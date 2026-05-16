package cl.duoc.airflytrip.chargingstations.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateChargingStationRequest {

    @NotBlank(message = "Charging station code is required")
    private String code;

    @NotBlank(message = "Charging station name is required")
    private String name;

    @NotNull(message = "Terminal id is required")
    private Long terminalId;

    @NotNull(message = "Capacity is required")
    @Min(value = 0, message = "Capacity must be greater than or equal to 0")
    private Integer capacity;

    @NotNull(message = "Available slots is required")
    @Min(value = 0, message = "Available slots must be greater than or equal to 0")
    private Integer availableSlots;

    private String status;

    private Boolean active;
}
