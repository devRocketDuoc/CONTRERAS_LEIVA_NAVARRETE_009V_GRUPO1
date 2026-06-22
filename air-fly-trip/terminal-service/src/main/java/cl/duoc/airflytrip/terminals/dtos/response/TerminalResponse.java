package cl.duoc.airflytrip.terminals.dtos.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Informacion resumida de un terminal del sistema.")
public class TerminalResponse {

    @Schema(description = "Identificador unico del terminal.", example = "4")
    private Long id;

    @Schema(description = "Nombre comercial del terminal.", example = "Terminal Alameda")
    private String name;

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
