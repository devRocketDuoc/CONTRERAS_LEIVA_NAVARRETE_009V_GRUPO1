package cl.duoc.airflytrip.vehicles.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import cl.duoc.airflytrip.vehicles.clients.ChargingStationClient;
import cl.duoc.airflytrip.vehicles.clients.TerminalClient;
import cl.duoc.airflytrip.vehicles.clients.response.ChargingStationResponse;
import cl.duoc.airflytrip.vehicles.clients.response.TerminalResponse;
import cl.duoc.airflytrip.vehicles.dtos.request.CreateVehicleRequest;
import cl.duoc.airflytrip.vehicles.dtos.request.UpdateVehicleBatteryRequest;
import cl.duoc.airflytrip.vehicles.dtos.response.VehicleResponse;
import cl.duoc.airflytrip.vehicles.exceptions.BadRequestException;
import cl.duoc.airflytrip.vehicles.models.Vehicle;
import cl.duoc.airflytrip.vehicles.repositories.VehicleRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

  @Mock
  private VehicleRepository vehicleRepository;

  @Mock
  private TerminalClient terminalClient;

  @Mock
  private ChargingStationClient chargingStationClient;

  @InjectMocks
  private VehicleService vehicleService;

  @Test
  void testCrearVehiculoExitoso() {

    CreateVehicleRequest request = new CreateVehicleRequest();
    request.setCode("VEH-001");
    request.setModel("BYD Dolphin");
    request.setBatteryPercentage(82);
    request.setTerminalId(3L);

    when(terminalClient.findById(3L)).thenReturn(terminalResponse(3L, true));
    when(vehicleRepository.existsByCodeIgnoreCase("VEH-001")).thenReturn(false);
    when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> {
      Vehicle vehicle = invocation.getArgument(0);
      vehicle.setId(10L);
      return vehicle;
    });

    VehicleResponse response = vehicleService.create(request);

    assertNotNull(response);
    assertEquals(10L, response.getId());
    assertEquals("VEH-001", response.getCode());
    assertEquals("AVAILABLE", response.getStatus());
    assertTrue(response.getActive());
  }

  @Test
  void testCrearVehiculoFallaCodigoDuplicado() {

    CreateVehicleRequest request = new CreateVehicleRequest();
    request.setCode("VEH-001");
    request.setModel("BYD Dolphin");
    request.setBatteryPercentage(82);
    request.setTerminalId(3L);

    when(terminalClient.findById(3L)).thenReturn(terminalResponse(3L, true));
    when(vehicleRepository.existsByCodeIgnoreCase("VEH-001")).thenReturn(true);

    Exception exception = null;

    try {
      vehicleService.create(request);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(BadRequestException.class, exception.getClass());
    assertEquals("Vehicle code already exists: VEH-001", exception.getMessage());
  }

  @Test
  void testActualizarBateriaFallaPorcentajeInvalido() {

    UpdateVehicleBatteryRequest request = new UpdateVehicleBatteryRequest();
    request.setBatteryPercentage(120);

    when(vehicleRepository.findById(5L)).thenReturn(Optional.of(vehicle(5L, true)));

    Exception exception = null;

    try {
      vehicleService.updateBattery(5L, request);
    } catch (Exception ex) {
      exception = ex;
    }

    assertNotNull(exception);
    assertEquals(BadRequestException.class, exception.getClass());
    assertEquals("Battery percentage must be between 0 and 100", exception.getMessage());
  }

  @Test
  void testEliminarVehiculoDesactivaRegistro() {

    Vehicle existing = vehicle(7L, true);

    when(vehicleRepository.findById(7L)).thenReturn(Optional.of(existing));
    when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

    vehicleService.delete(7L);

    assertFalse(existing.getActive());
  }

  private Vehicle vehicle(Long id, Boolean active) {
    return Vehicle.builder()
        .id(id)
        .code("VEH-001")
        .model("BYD Dolphin")
        .status("AVAILABLE")
        .batteryPercentage(82)
        .terminalId(3L)
        .chargingStationId(7L)
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

  @SuppressWarnings("unused")
  private ChargingStationResponse chargingStationResponse(Long id, Boolean active) {
    ChargingStationResponse response = new ChargingStationResponse();
    response.setId(id);
    response.setCode("CS-" + id);
    response.setName("Estacion " + id);
    response.setTerminalId(3L);
    response.setCapacity(10);
    response.setAvailableSlots(5);
    response.setStatus("AVAILABLE");
    response.setActive(active);
    return response;
  }
}
