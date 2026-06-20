package cl.duoc.airflytrip.auth.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta entregada luego de una autenticacion exitosa.")
public class AuthResponse {

    @Schema(description = "Token JWT emitido para el usuario autenticado.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbmEucGVyZXpAYWlyZmx5dHJpcC5jbCIsInJvbGUiOiJDTElFTlQifQ.firma")
    private String token;

    @Schema(description = "Tipo de token devuelto por el servicio.", example = "Bearer")
    private String tokenType;

    @Schema(
            description = "Informacion del usuario autenticado.",
            example = "{\"id\":15,\"email\":\"ana.perez@airflytrip.cl\",\"firstName\":\"Ana\",\"lastName\":\"Perez\",\"documentNumber\":\"12345678-9\",\"phone\":\"+56912345678\",\"role\":\"CLIENT\",\"status\":\"ACTIVE\",\"enabled\":true,\"createdAt\":\"2026-06-20T10:30:00\"}"
    )
    private UserResponse user;
}
