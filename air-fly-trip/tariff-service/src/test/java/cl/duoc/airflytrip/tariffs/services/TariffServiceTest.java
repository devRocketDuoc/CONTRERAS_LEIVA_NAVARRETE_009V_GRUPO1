package cl.duoc.airflytrip.tariffs.services;

import cl.duoc.airflytrip.tariffs.clients.RouteClient;
import cl.duoc.airflytrip.tariffs.clients.response.RouteResponse;
import cl.duoc.airflytrip.tariffs.dtos.request.CreateTariffRequest;
import cl.duoc.airflytrip.tariffs.dtos.request.UpdateTariffRequest;
import cl.duoc.airflytrip.tariffs.dtos.response.TariffResponse;
import cl.duoc.airflytrip.tariffs.exceptions.BadRequestException;
import cl.duoc.airflytrip.tariffs.models.Tariff;
import cl.duoc.airflytrip.tariffs.repositories.TariffRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TariffServiceTest {

    @Mock
    private TariffRepository tariffRepository;

    @Mock
    private RouteClient routeClient;

    @InjectMocks
    private TariffService tariffService;

    @Test
    void createShouldPersistTariffSuccessfully() {

        CreateTariffRequest request = new CreateTariffRequest();
        request.setRouteId(8L);
        request.setBasePrice(new BigDecimal("3500.00"));
        request.setPricePerKm(new BigDecimal("120.50"));
        request.setVehicleType("ELECTRIC_VAN");

        when(routeClient.findById(8L)).thenReturn(activeRoute(8L));
        when(tariffRepository.existsByRouteIdAndVehicleTypeIgnoreCase(8L, "ELECTRIC_VAN")).thenReturn(false);
        when(tariffRepository.save(any(Tariff.class))).thenAnswer(invocation -> {
            Tariff tariff = invocation.getArgument(0);
            tariff.setId(21L);
            return tariff;
        });

        TariffResponse response = tariffService.create(request);

        assertNotNull(response);
        assertEquals(21L, response.getId());
        assertEquals("ELECTRIC_VAN", response.getVehicleType());
        assertEquals(true, response.getActive());
    }

    @Test
    void createShouldFailWhenTariffAlreadyExists() {

        CreateTariffRequest request = new CreateTariffRequest();
        request.setRouteId(8L);
        request.setBasePrice(new BigDecimal("3500.00"));
        request.setPricePerKm(new BigDecimal("120.50"));
        request.setVehicleType("ELECTRIC_VAN");

        when(routeClient.findById(8L)).thenReturn(activeRoute(8L));
        when(tariffRepository.existsByRouteIdAndVehicleTypeIgnoreCase(8L, "ELECTRIC_VAN")).thenReturn(true);

        Exception exception = null;

        try {
            tariffService.create(request);
        } catch (Exception ex) {
            exception = ex;
        }

        assertNotNull(exception);
        assertEquals(BadRequestException.class, exception.getClass());
        assertEquals("Tariff already exists for route 8 and vehicle type ELECTRIC_VAN", exception.getMessage());
    }

    @Test
    void updateShouldModifyExistingTariff() {

        Tariff tariff = sampleTariff(21L, true);

        UpdateTariffRequest request = new UpdateTariffRequest();
        request.setRouteId(9L);
        request.setBasePrice(new BigDecimal("4200.00"));
        request.setPricePerKm(new BigDecimal("145.00"));
        request.setVehicleType("ELECTRIC_BUS");
        request.setActive(false);

        when(tariffRepository.findById(21L)).thenReturn(Optional.of(tariff));
        when(routeClient.findById(9L)).thenReturn(activeRoute(9L));
        when(tariffRepository.save(any(Tariff.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TariffResponse response = tariffService.update(21L, request);

        assertNotNull(response);
        assertEquals(9L, response.getRouteId());
        assertEquals("ELECTRIC_BUS", response.getVehicleType());
        assertEquals(false, response.getActive());
    }

    @Test
    void deleteShouldDeactivateTariff() {

        Tariff tariff = sampleTariff(21L, true);

        when(tariffRepository.findById(21L)).thenReturn(Optional.of(tariff));
        when(tariffRepository.save(any(Tariff.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tariffService.delete(21L);

        assertFalse(tariff.getActive());
        verify(tariffRepository).save(tariff);
    }

    private RouteResponse activeRoute(Long id) {
        RouteResponse routeResponse = new RouteResponse();
        routeResponse.setId(id);
        routeResponse.setActive(true);
        return routeResponse;
    }

    private Tariff sampleTariff(Long id, Boolean active) {
        return Tariff.builder()
                .id(id)
                .routeId(8L)
                .basePrice(new BigDecimal("3500.00"))
                .pricePerKm(new BigDecimal("120.50"))
                .vehicleType("ELECTRIC_VAN")
                .active(active)
                .build();
    }
}
