package cl.duoc.airflytrip.tariffs.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para actualizar una tarifa existente.")
public class UpdateTariffRequest {

    @NotNull(message = "Route id is required")
    @Schema(description = "Identificador de la ruta asociada a la tarifa.", example = "8")
    private Long routeId;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.00", message = "Base price must be greater than or equal to 0")
    @Schema(description = "Nuevo precio base de la tarifa.", example = "4200.00")
    private BigDecimal basePrice;

    @NotNull(message = "Price per km is required")
    @DecimalMin(value = "0.00", message = "Price per km must be greater than or equal to 0")
    @Schema(description = "Nuevo costo por kilometro recorrido.", example = "145.00")
    private BigDecimal pricePerKm;

    @NotBlank(message = "Vehicle type is required")
    @Schema(description = "Nuevo tipo de vehiculo asociado a la tarifa.", example = "ELECTRIC_BUS")
    private String vehicleType;

    @Schema(description = "Indica si la tarifa permanecera activa.", example = "true")
    private Boolean active;
}
