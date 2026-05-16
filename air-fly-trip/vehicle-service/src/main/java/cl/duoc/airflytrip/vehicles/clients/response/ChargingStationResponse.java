package cl.duoc.airflytrip.vehicles.clients.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChargingStationResponse {

    private Long id;
    private String code;
    private String name;
    private Long terminalId;
    private Integer capacity;
    private Integer availableSlots;
    private String status;
    private Boolean active;
}
