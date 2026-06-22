package cl.duoc.airflytrip.tariffs.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Respuesta con la informacion detallada de una tarifa.")
public class TariffResponse {

    @Schema(description = "Identificador unico de la tarifa.", example = "21")
    private Long id;

    @Schema(description = "Identificador de la ruta asociada a la tarifa.", example = "8")
    private Long routeId;

    @Schema(description = "Precio base configurado para la tarifa.", example = "3500.00")
    private BigDecimal basePrice;

    @Schema(description = "Costo configurado por kilometro.", example = "120.50")
    private BigDecimal pricePerKm;

    @Schema(description = "Tipo de vehiculo al que aplica la tarifa.", example = "ELECTRIC_VAN")
    private String vehicleType;

    @Schema(description = "Indica si la tarifa se encuentra activa.", example = "true")
    private Boolean active;
}
