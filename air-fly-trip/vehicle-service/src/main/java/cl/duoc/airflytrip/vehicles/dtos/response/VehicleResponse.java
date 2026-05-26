package cl.duoc.airflytrip.vehicles.dtos.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
