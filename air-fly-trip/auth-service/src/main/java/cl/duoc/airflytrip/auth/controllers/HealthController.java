package cl.duoc.airflytrip.auth.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name = "Monitoreo del servicio",
        description = "Operacion simple para verificar que el microservicio de autenticacion se encuentra disponible."
)
public class HealthController {

    @GetMapping("/health")
    @Operation(
            summary = "Consultar estado del servicio",
            description = "Retorna un mensaje simple para confirmar que auth-service se encuentra operativo."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Servicio operativo",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class, example = "auth-service is running"))
            )
    })
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("auth-service is running");
    }
}
