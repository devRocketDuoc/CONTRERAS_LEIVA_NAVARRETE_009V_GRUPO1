package cl.duoc.airflytrip.vehicles.controllers;

import cl.duoc.airflytrip.vehicles.dtos.request.CreateVehicleRequest;
import cl.duoc.airflytrip.vehicles.dtos.request.UpdateVehicleBatteryRequest;
import cl.duoc.airflytrip.vehicles.dtos.response.VehicleResponse;
import cl.duoc.airflytrip.vehicles.services.VehicleService;
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
class VehicleControllerTest {

  @Mock
  private VehicleService vehicleService;

  @InjectMocks
  private VehicleController vehicleController;

  @Test
  void findAllShouldReturnVehicles() {

    when(vehicleService.findAll()).thenReturn(List.of(vehicleResponse(1L, "VEH-001", "AVAILABLE", true)));

    ResponseEntity<List<VehicleResponse>> result = vehicleController.findAll();

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(1, result.getBody().size());
    assertEquals("VEH-001", result.getBody().get(0).getCode());
  }

  @Test
  void createShouldReturnCreatedVehicle() {

    CreateVehicleRequest request = new CreateVehicleRequest();
    request.setCode("VEH-001");
    request.setModel("BYD Dolphin");
    request.setBatteryPercentage(82);
    request.setTerminalId(3L);

    VehicleResponse responseSimulado = vehicleResponse(10L, "VEH-001", "AVAILABLE", true);

    when(vehicleService.create(any(CreateVehicleRequest.class))).thenReturn(responseSimulado);

    ResponseEntity<VehicleResponse> result = vehicleController.create(request);

    assertEquals(HttpStatus.CREATED, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(10L, result.getBody().getId());
    assertEquals("VEH-001", result.getBody().getCode());
  }

  @Test
  void updateBatteryShouldReturnUpdatedVehicle() {

    UpdateVehicleBatteryRequest request = new UpdateVehicleBatteryRequest();
    request.setBatteryPercentage(60);

    VehicleResponse responseSimulado = vehicleResponse(5L, "VEH-005", "AVAILABLE", true);
    responseSimulado = VehicleResponse.builder()
        .id(responseSimulado.getId())
        .code(responseSimulado.getCode())
        .model(responseSimulado.getModel())
        .status(responseSimulado.getStatus())
        .batteryPercentage(60)
        .terminalId(responseSimulado.getTerminalId())
        .chargingStationId(responseSimulado.getChargingStationId())
        .active(responseSimulado.getActive())
        .build();

    when(vehicleService.updateBattery(5L, request)).thenReturn(responseSimulado);

    ResponseEntity<VehicleResponse> result = vehicleController.updateBattery(5L, request);

    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNotNull(result.getBody());
    assertEquals(60, result.getBody().getBatteryPercentage());
  }

  @Test
  void deleteShouldReturnNoContent() {

    ResponseEntity<Void> result = vehicleController.delete(7L);

    assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
  }

  private VehicleResponse vehicleResponse(Long id, String code, String status, Boolean active) {
    return VehicleResponse.builder()
        .id(id)
        .code(code)
        .model("BYD Dolphin")
        .status(status)
        .batteryPercentage(82)
        .terminalId(3L)
        .chargingStationId(7L)
        .active(active)
        .build();
  }
}
