package cl.duoc.airflytrip.tariffs.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateTariffRequest {

    @NotNull(message = "Route id is required")
    private Long routeId;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.00", message = "Base price must be greater than or equal to 0")
    private BigDecimal basePrice;

    @NotNull(message = "Price per km is required")
    @DecimalMin(value = "0.00", message = "Price per km must be greater than or equal to 0")
    private BigDecimal pricePerKm;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    private Boolean active;
}