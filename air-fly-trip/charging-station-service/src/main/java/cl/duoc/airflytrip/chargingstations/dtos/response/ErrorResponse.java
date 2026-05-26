package cl.duoc.airflytrip.chargingstations.dtos.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private int status;

    private String error;

    private String message;

    private LocalDateTime timestamp;
}
