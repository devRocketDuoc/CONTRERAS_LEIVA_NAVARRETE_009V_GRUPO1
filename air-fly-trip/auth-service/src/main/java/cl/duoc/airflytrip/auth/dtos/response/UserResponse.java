package cl.duoc.airflytrip.auth.dtos.response;

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
