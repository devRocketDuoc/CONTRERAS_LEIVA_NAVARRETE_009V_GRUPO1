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
@Schema(description = "Datos requeridos para crear una nueva tarifa en el sistema.")
public class CreateTariffRequest {

    @NotNull(message = "Route id is required")
    @Schema(description = "Identificador de la ruta a la que se asociara la tarifa.", example = "8")
    private Long routeId;

    @NotNull(message = "Base price is required")
    @DecimalMin(value = "0.00", message = "Base price must be greater than or equal to 0")
    @Schema(description = "Precio base aplicado a la tarifa.", example = "3500.00")
    private BigDecimal basePrice;

    @NotNull(message = "Price per km is required")
    @DecimalMin(value = "0.00", message = "Price per km must be greater than or equal to 0")
    @Schema(description = "Costo adicional por kilometro recorrido.", example = "120.50")
    private BigDecimal pricePerKm;

    @NotBlank(message = "Vehicle type is required")
    @Schema(description = "Tipo de vehiculo al que aplica la tarifa.", example = "ELECTRIC_VAN")
    private String vehicleType;

    @Schema(description = "Indica si la tarifa quedara activa despues de su creacion.", example = "true")
    private Boolean active;
}
