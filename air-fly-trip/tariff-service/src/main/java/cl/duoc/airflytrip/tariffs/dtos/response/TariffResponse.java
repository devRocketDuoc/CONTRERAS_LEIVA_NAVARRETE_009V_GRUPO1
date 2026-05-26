package cl.duoc.airflytrip.tariffs.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TariffResponse {

    private Long id;

    private Long routeId;

    private BigDecimal basePrice;

    private BigDecimal pricePerKm;

    private String vehicleType;

    private Boolean active;
}