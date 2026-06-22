package cl.duoc.airflytrip.chargingstations.controllers;

import cl.duoc.airflytrip.chargingstations.dtos.request.CreateChargingStationRequest;
import cl.duoc.airflytrip.chargingstations.dtos.request.UpdateChargingStationStatusRequest;
import cl.duoc.airflytrip.chargingstations.dtos.response.ChargingStationResponse;
import cl.duoc.airflytrip.chargingstations.services.ChargingStationService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChargingStationControllerTest {

  @Mock
  private ChargingStationService chargingStationService;

  @InjectMocks
  private ChargingStationController chargingStationController;

  @Test
  void findAllShouldReturnChargingStations() {

    when(chargingStationService.findAll()).thenReturn(List.of(chargingStationResponse(1L, "AVAILABLE", true)));

    ResponseEntity<List<ChargingStationResponse>> result = chargingStationController.findAll();

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(1, result.getBody().size());
    assertEquals(1L, result.getBody().get(0).getId());
  }

  @Test
  void createShouldReturnCreatedChargingStation() {

    CreateChargingStationRequest request = new CreateChargingStationRequest();
    request.setCode("CS-015");
    request.setName("Estacion Centro");
    request.setTerminalId(3L);
    request.setCapacity(12);
    request.setAvailableSlots(5);

    ChargingStationResponse responseSimulado = chargingStationResponse(15L, "AVAILABLE", true);

    when(chargingStationService.create(any(CreateChargingStationRequest.class))).thenReturn(responseSimulado);

    ResponseEntity<ChargingStationResponse> result = chargingStationController.create(request);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(15L, result.getBody().getId());
    assertEquals("CS-015", result.getBody().getCode());
  }

  @Test
  void updateStatusShouldReturnUpdatedChargingStation() {

    UpdateChargingStationStatusRequest request = new UpdateChargingStationStatusRequest();
    request.setStatus("MAINTENANCE");

    ChargingStationResponse responseSimulado = chargingStationResponse(15L, "MAINTENANCE", true);

    when(chargingStationService.updateStatus(15L, request)).thenReturn(responseSimulado);

    ResponseEntity<ChargingStationResponse> result = chargingStationController.updateStatus(15L, request);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals("MAINTENANCE", result.getBody().getStatus());
  }

  @Test
  void deleteShouldReturnNoContent() {

    ResponseEntity<Void> result = chargingStationController.delete(15L);

    assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
  }

  private ChargingStationResponse chargingStationResponse(Long id, String status, Boolean active) {
    return ChargingStationResponse.builder()
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
}
