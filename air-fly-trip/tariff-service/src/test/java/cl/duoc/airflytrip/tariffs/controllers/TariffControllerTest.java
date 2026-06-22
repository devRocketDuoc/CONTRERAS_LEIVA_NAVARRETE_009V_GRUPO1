package cl.duoc.airflytrip.tariffs.controllers;

import cl.duoc.airflytrip.tariffs.dtos.request.CreateTariffRequest;
import cl.duoc.airflytrip.tariffs.dtos.request.UpdateTariffRequest;
import cl.duoc.airflytrip.tariffs.dtos.response.TariffResponse;
import cl.duoc.airflytrip.tariffs.services.TariffService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TariffControllerTest {

    @Mock
    private TariffService tariffService;

    @InjectMocks
    private TariffController tariffController;

    @Test
    void createShouldReturnCreatedTariff() {

        CreateTariffRequest request = new CreateTariffRequest();
        request.setRouteId(8L);
        request.setBasePrice(new BigDecimal("3500.00"));
        request.setPricePerKm(new BigDecimal("120.50"));
        request.setVehicleType("ELECTRIC_VAN");

        TariffResponse response = tariffResponse(21L, true);

        when(tariffService.create(any(CreateTariffRequest.class))).thenReturn(response);

        ResponseEntity<TariffResponse> result = tariffController.create(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(21L, result.getBody().getId());
    }

    @Test
    void findAllShouldReturnTariffList() {

        when(tariffService.findAll()).thenReturn(List.of(
                tariffResponse(21L, true),
                tariffResponse(22L, false)
        ));

        ResponseEntity<List<TariffResponse>> result = tariffController.findAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
    }

    @Test
    void updateShouldReturnUpdatedTariff() {

        UpdateTariffRequest request = new UpdateTariffRequest();
        request.setRouteId(8L);
        request.setBasePrice(new BigDecimal("4200.00"));
        request.setPricePerKm(new BigDecimal("145.00"));
        request.setVehicleType("ELECTRIC_BUS");
        request.setActive(false);

        TariffResponse response = tariffResponse(21L, false);

        when(tariffService.update(eq(21L), any(UpdateTariffRequest.class))).thenReturn(response);

        ResponseEntity<TariffResponse> result = tariffController.update(21L, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(false, result.getBody().getActive());
    }

    @Test
    void deleteShouldReturnNoContent() {

        doNothing().when(tariffService).delete(21L);

        ResponseEntity<Void> result = tariffController.delete(21L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    private TariffResponse tariffResponse(Long id, Boolean active) {
        return TariffResponse.builder()
                .id(id)
                .routeId(8L)
                .basePrice(new BigDecimal("3500.00"))
                .pricePerKm(new BigDecimal("120.50"))
                .vehicleType("ELECTRIC_VAN")
                .active(active)
                .build();
    }
}
