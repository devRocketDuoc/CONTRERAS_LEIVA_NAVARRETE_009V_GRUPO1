package cl.duoc.airflytrip.chargingstations.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cl.duoc.airflytrip.chargingstations.clients.TerminalClient;
import cl.duoc.airflytrip.chargingstations.clients.response.TerminalResponse;
import cl.duoc.airflytrip.chargingstations.dtos.request.CreateChargingStationRequest;
import cl.duoc.airflytrip.chargingstations.dtos.request.UpdateChargingStationStatusRequest;
import cl.duoc.airflytrip.chargingstations.dtos.response.ChargingStationResponse;
import cl.duoc.airflytrip.chargingstations.exceptions.BadRequestException;
import cl.duoc.airflytrip.chargingstations.models.ChargingStation;
import cl.duoc.airflytrip.chargingstations.repositories.ChargingStationRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChargingStationServiceTest {

  @Mock
  private ChargingStationRepository chargingStationRepository;

  @Mock
  private TerminalClient terminalClient;

  @InjectMocks
  private ChargingStationService chargingStationService;

  @Test
  void testCrearEstacionDeCargaExitosa() {

    CreateChargingStationRequest request = new CreateChargingStationRequest();
    request.setCode("CS-015");
    request.setName("Estacion Centro");
    request.setTerminalId(3L);
    request.setCapacity(12);
    request.setAvailableSlots(5);

    when(terminalClient.findById(3L)).thenReturn(terminalResponse(3L, true));
    when(chargingStationRepository.existsByCodeIgnoreCase("CS-015")).thenReturn(false);
    when(chargingStationRepository.save(any(ChargingStation.class))).thenAnswer(invocation -> {
      ChargingStation station = invocation.getArgument(0);
      station.setId(15L);
      return station;
    });

    ChargingStationResponse response = chargingStationService.create(request);

    assertNotNull(response);
    assertEquals(15L, response.getId());
    assertEquals("AVAILABLE", response.getStatus());
    assertTrue(response.getActive());
  }

  @Test
  void testCrearEstacionDeCargaFallaCodigoDuplicado() {

    CreateChargingStationRequest request = new CreateChargingStationRequest();
    request.setCode("CS-015");
    request.setName("Estacion Centro");
    request.setTerminalId(3L);
    request.setCapacity(12);
    request.setAvailableSlots(5);

    when(terminalClient.findById(3L)).thenReturn(terminalResponse(3L, true));
    when(chargingStationRepository.existsByCodeIgnoreCase("CS-015")).thenReturn(true);

    Exception exception = null;

    try {
      chargingStationService.create(request);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(BadRequestException.class, exception.getClass());
    assertEquals("Charging station code already exists: CS-015", exception.getMessage());
  }

  @Test
  void testActualizarEstadoExitoso() {

    UpdateChargingStationStatusRequest request = new UpdateChargingStationStatusRequest();
    request.setStatus("MAINTENANCE");

    ChargingStation existing = chargingStation(15L, "AVAILABLE", true);

    when(chargingStationRepository.findById(15L)).thenReturn(Optional.of(existing));
    when(chargingStationRepository.save(any(ChargingStation.class))).thenAnswer(invocation -> invocation.getArgument(0));

    ChargingStationResponse response = chargingStationService.updateStatus(15L, request);

    assertNotNull(response);
    assertEquals("MAINTENANCE", response.getStatus());
  }

  @Test
  void testEliminarEstacionDeCargaDesactivaRegistro() {

    ChargingStation existing = chargingStation(15L, "AVAILABLE", true);

    when(chargingStationRepository.findById(15L)).thenReturn(Optional.of(existing));
    when(chargingStationRepository.save(any(ChargingStation.class))).thenAnswer(invocation -> invocation.getArgument(0));

    chargingStationService.delete(15L);

    assertFalse(existing.getActive());
  }

  private ChargingStation chargingStation(Long id, String status, Boolean active) {
    return ChargingStation.builder()
        .id(id)
        .code("CS-015")
        .name("Estacion Centro")
        .terminalId(3L)
        .capacity(12)
        .availableSlots(5)
        .status(status)
        .active(active)
        .build();
  }

  private TerminalResponse terminalResponse(Long id, Boolean active) {
    TerminalResponse response = new TerminalResponse();
    response.setId(id);
    response.setName("Terminal " + id);
    response.setCity("Santiago");
    response.setLocationDescription("Ubicacion " + id);
    response.setActive(active);
    return response;
  }
}
