package cl.duoc.airflytrip.chargingstations.dtos.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
