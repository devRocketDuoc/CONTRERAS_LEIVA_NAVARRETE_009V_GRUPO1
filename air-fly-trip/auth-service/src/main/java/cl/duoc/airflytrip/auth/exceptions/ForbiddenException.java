package cl.duoc.airflytrip.auth.exceptions;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
