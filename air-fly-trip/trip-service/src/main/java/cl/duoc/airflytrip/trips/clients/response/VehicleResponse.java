package cl.duoc.airflytrip.trips.clients.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleResponse {

    private Long id;
    private String code;
    private String model;
    private String status;
    private Integer batteryPercentage;
    private Long terminalId;
    private Long chargingStationId;
    private Boolean active;
}
