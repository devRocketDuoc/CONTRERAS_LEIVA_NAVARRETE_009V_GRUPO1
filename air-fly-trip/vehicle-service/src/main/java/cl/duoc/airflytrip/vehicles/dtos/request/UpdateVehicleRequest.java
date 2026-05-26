package cl.duoc.airflytrip.vehicles.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateVehicleRequest {

    @NotBlank(message = "Vehicle code is required")
    private String code;

    @NotBlank(message = "Vehicle model is required")
    private String model;

    private String status;

    @NotNull(message = "Battery percentage is required")
    @Min(value = 0, message = "Battery percentage must be greater than or equal to 0")
    @Max(value = 100, message = "Battery percentage must be less than or equal to 100")
    private Integer batteryPercentage;

    @NotNull(message = "Terminal id is required")
    private Long terminalId;

    private Long chargingStationId;

    private Boolean active;
}