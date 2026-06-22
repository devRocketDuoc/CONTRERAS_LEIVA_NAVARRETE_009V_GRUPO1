package cl.duoc.airflytrip.auth.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
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
@Schema(description = "Informacion resumida de un usuario del microservicio de autenticacion.")
public class UserResponse {

    @Schema(description = "Identificador unico del usuario.", example = "15")
    private Long id;

    @Schema(description = "Correo electronico del usuario.", example = "ana.perez@airflytrip.cl")
    private String email;

    @Schema(description = "Nombre del usuario.", example = "Ana")
    private String firstName;

    @Schema(description = "Apellido del usuario.", example = "Perez")
    private String lastName;

    @Schema(description = "Numero de documento o identificacion del usuario.", example = "12345678-9")
    private String documentNumber;

    @Schema(description = "Telefono de contacto del usuario.", example = "+56912345678")
    private String phone;

    @Schema(description = "Rol asignado al usuario.", example = "CLIENT")
    private String role;

    @Schema(description = "Estado actual del usuario.", example = "ACTIVE")
    private String status;

    @Schema(description = "Indica si la cuenta se encuentra habilitada.", example = "true")
    private Boolean enabled;

    @Schema(description = "Fecha y hora de creacion del usuario.", example = "2026-06-20T10:30:00")
    private LocalDateTime createdAt;
}
