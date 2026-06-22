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
@Schema(description = "Datos requeridos para registrar una nueva cuenta de cliente en auth-service.")
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(description = "Correo electronico unico del nuevo cliente.", example = "ana.perez@airflytrip.cl")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must have at least 6 characters")
    @Schema(description = "Contrasena del nuevo cliente con al menos 6 caracteres.", example = "ClaveSegura123")
    private String password;

    @NotBlank(message = "First name is required")
    @Schema(description = "Nombre del cliente.", example = "Ana")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Apellido del cliente.", example = "Perez")
    private String lastName;

    @Schema(description = "Numero de documento o identificacion del cliente.", example = "12345678-9")
    private String documentNumber;

    @Schema(description = "Telefono de contacto del cliente.", example = "+56912345678")
    private String phone;

    @Schema(description = "Rol solicitado en el registro publico. Actualmente el registro crea usuarios con rol CLIENT.", example = "CLIENT")
    private String role;
}
