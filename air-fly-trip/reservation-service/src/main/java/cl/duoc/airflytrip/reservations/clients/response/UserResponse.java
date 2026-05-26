package cl.duoc.airflytrip.reservations.clients.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String documentNumber;
    private String phone;
    private String role;
    private String status;
    private Boolean enabled;
    private LocalDateTime createdAt;
}
