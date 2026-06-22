package cl.duoc.airflytrip.auth.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Datos requeridos para crear un usuario desde una cuenta autenticada.")
public class CreateUserRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Correo electronico unico del usuario a crear.", example = "operador.sur@airflytrip.cl")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    @Schema(description = "Contrasena inicial del usuario a crear.", example = "ClaveTemporal123")
    private String password;

    @NotBlank(message = "First name is required")
    @Schema(description = "Nombre del usuario a crear.", example = "Camila")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Apellido del usuario a crear.", example = "Gonzalez")
    private String lastName;

    @Schema(description = "Numero de documento o identificacion del usuario.", example = "98765432-1")
    private String documentNumber;

    @Schema(description = "Telefono de contacto del usuario.", example = "+56987654321")
    private String phone;

    @Schema(description = "Rol del usuario a crear. Si se omite, se asigna CLIENT.", example = "OPERATOR")
    private String role;
}
