package cl.duoc.airflytrip.auth.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Datos requeridos para autenticar a un usuario en auth-service.")
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Correo electronico registrado del usuario.", example = "ana.perez@airflytrip.cl")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Contrasena del usuario para iniciar sesion.", example = "ClaveSegura123")
    private String password;
}
