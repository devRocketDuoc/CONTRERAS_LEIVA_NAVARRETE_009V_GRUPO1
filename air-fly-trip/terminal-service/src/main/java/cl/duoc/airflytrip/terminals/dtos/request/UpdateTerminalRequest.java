package cl.duoc.airflytrip.terminals.dtos.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para actualizar completamente un terminal existente.")
public class UpdateTerminalRequest {

    @NotBlank(message = "Terminal name is required")
    @Schema(description = "Nombre comercial del terminal.", example = "Terminal Pajaritos")
    private String name;

    @NotBlank(message = "Terminal city is required")
    @Schema(description = "Ciudad donde se ubica el terminal.", example = "Santiago")
    private String city;

    @Schema(description = "Descripcion de la ubicacion del terminal.", example = "Av. General Oscar Bonilla 2250")
    private String locationDescription;

    @Schema(description = "Latitud geografica del terminal.", example = "-33.4445")
    private BigDecimal latitude;

    @Schema(description = "Longitud geografica del terminal.", example = "-70.7193")
    private BigDecimal longitude;

    @Schema(description = "Indica si el terminal permanece activo.", example = "true")
    private Boolean active;
}
