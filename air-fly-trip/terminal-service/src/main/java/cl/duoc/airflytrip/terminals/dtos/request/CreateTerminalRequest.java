package cl.duoc.airflytrip.terminals.dtos.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para crear un nuevo terminal en el sistema.")
public class CreateTerminalRequest {

    @NotBlank(message = "Terminal name is required")
    @Schema(description = "Nombre comercial del terminal.", example = "Terminal Alameda")
    private String name;

    @NotBlank(message = "Terminal city is required")
    @Schema(description = "Ciudad donde se ubica el terminal.", example = "Santiago")
    private String city;

    @Schema(description = "Descripcion de la ubicacion del terminal.", example = "Av. Libertador Bernardo O'Higgins 3850")
    private String locationDescription;

    @Schema(description = "Latitud geografica del terminal.", example = "-33.4521")
    private BigDecimal latitude;

    @Schema(description = "Longitud geografica del terminal.", example = "-70.6795")
    private BigDecimal longitude;

    @Schema(description = "Indica si el terminal se encuentra activo.", example = "true")
    private Boolean active;
}
