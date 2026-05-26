package cl.duoc.airflytrip.vehicles.services;

import cl.duoc.airflytrip.vehicles.clients.ChargingStationClient;
import cl.duoc.airflytrip.vehicles.clients.TerminalClient;
import cl.duoc.airflytrip.vehicles.clients.response.ChargingStationResponse;
import cl.duoc.airflytrip.vehicles.clients.response.TerminalResponse;
import cl.duoc.airflytrip.vehicles.dtos.request.CreateVehicleRequest;
import cl.duoc.airflytrip.vehicles.dtos.request.UpdateVehicleBatteryRequest;
import cl.duoc.airflytrip.vehicles.dtos.request.UpdateVehicleRequest;
import cl.duoc.airflytrip.vehicles.dtos.request.UpdateVehicleStatusRequest;
import cl.duoc.airflytrip.vehicles.dtos.response.VehicleResponse;
import cl.duoc.airflytrip.vehicles.exceptions.BadRequestException;
import cl.duoc.airflytrip.vehicles.exceptions.NotFoundException;
import cl.duoc.airflytrip.vehicles.exceptions.RemoteServiceException;
import cl.duoc.airflytrip.vehicles.models.Vehicle;
import cl.duoc.airflytrip.vehicles.repositories.VehicleRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleService {

    private static final String DEFAULT_STATUS = "AVAILABLE";

    private final VehicleRepository vehicleRepository;
    private final TerminalClient terminalClient;
    private final ChargingStationClient chargingStationClient;

    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<VehicleResponse> findActive() {
        return vehicleRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    public List<VehicleResponse> findAvailable() {
        return vehicleRepository.findByStatusIgnoreCase("AVAILABLE").stream().map(this::toResponse).toList();
    }

    public List<VehicleResponse> findByTerminalId(Long terminalId) {
        validateTerminalExists(terminalId);
        return vehicleRepository.findByTerminalId(terminalId).stream().map(this::toResponse).toList();
    }

    public VehicleResponse findById(Long id) {
        return toResponse(findVehicleById(id));
    }

    public VehicleResponse create(CreateVehicleRequest request) {
        validateTerminalExists(request.getTerminalId());
        validateChargingStationIfPresent(request.getChargingStationId());
        validateBattery(request.getBatteryPercentage());
        validateCodeDoesNotExist(request.getCode());

        Vehicle vehicle = Vehicle.builder()
                .code(request.getCode())
                .model(request.getModel())
                .status(request.getStatus() != null ? request.getStatus() : DEFAULT_STATUS)
                .batteryPercentage(request.getBatteryPercentage())
                .terminalId(request.getTerminalId())
                .chargingStationId(request.getChargingStationId())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.info(
                "event=vehicle_created service=vehicle-service vehicle_id={} terminal_id={} charging_station_id={}",
                savedVehicle.getId(),
                savedVehicle.getTerminalId(),
                savedVehicle.getChargingStationId()
        );
        return toResponse(savedVehicle);
    }

    public VehicleResponse update(Long id, UpdateVehicleRequest request) {
        Vehicle vehicle = findVehicleById(id);

        validateTerminalExists(request.getTerminalId());
        validateChargingStationIfPresent(request.getChargingStationId());
        validateBattery(request.getBatteryPercentage());

        vehicle.setCode(request.getCode());
        vehicle.setModel(request.getModel());
        vehicle.setStatus(request.getStatus() != null ? request.getStatus() : vehicle.getStatus());
        vehicle.setBatteryPercentage(request.getBatteryPercentage());
        vehicle.setTerminalId(request.getTerminalId());
        vehicle.setChargingStationId(request.getChargingStationId());
        vehicle.setActive(request.getActive() != null ? request.getActive() : vehicle.getActive());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        log.info(
                "event=vehicle_updated service=vehicle-service vehicle_id={} status={} battery={}",
                updatedVehicle.getId(),
                updatedVehicle.getStatus(),
                updatedVehicle.getBatteryPercentage()
        );
        return toResponse(updatedVehicle);
    }

    public VehicleResponse updateStatus(Long id, UpdateVehicleStatusRequest request) {
        Vehicle vehicle = findVehicleById(id);
        vehicle.setStatus(request.getStatus());
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        log.info("event=vehicle_status_updated service=vehicle-service vehicle_id={} status={}", id, request.getStatus());
        return toResponse(updatedVehicle);
    }

    public VehicleResponse updateBattery(Long id, UpdateVehicleBatteryRequest request) {
        Vehicle vehicle = findVehicleById(id);
        validateBattery(request.getBatteryPercentage());
        vehicle.setBatteryPercentage(request.getBatteryPercentage());
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        log.info("event=vehicle_battery_updated service=vehicle-service vehicle_id={} battery={}", id, request.getBatteryPercentage());
        return toResponse(updatedVehicle);
    }

    public void delete(Long id) {
        Vehicle vehicle = findVehicleById(id);
        vehicle.setActive(false);
        vehicleRepository.save(vehicle);
        log.info("event=vehicle_deleted service=vehicle-service vehicle_id={}", id);
    }

    private Vehicle findVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found with id: " + id));
    }

    private void validateTerminalExists(Long terminalId) {
        try {
            TerminalResponse terminal = terminalClient.findById(terminalId);
            if (terminal == null || Boolean.FALSE.equals(terminal.getActive())) {
                throw new BadRequestException("Terminal is not active or does not exist with id: " + terminalId);
            }
        } catch (FeignException.NotFound exception) {
            throw new BadRequestException("Terminal not found with id: " + terminalId);
        } catch (FeignException exception) {
            throw new RemoteServiceException("Error communicating with terminal-service");
        }
    }

    private void validateChargingStationIfPresent(Long chargingStationId) {
        if (chargingStationId == null) {
            return;
        }

        try {
            ChargingStationResponse station = chargingStationClient.findById(chargingStationId);
            if (station == null || Boolean.FALSE.equals(station.getActive())) {
                throw new BadRequestException("Charging station is not active or does not exist with id: " + chargingStationId);
            }
        } catch (FeignException.NotFound exception) {
            throw new BadRequestException("Charging station not found with id: " + chargingStationId);
        } catch (FeignException exception) {
            throw new RemoteServiceException("Error communicating with charging-station-service");
        }
    }

    private void validateBattery(Integer batteryPercentage) {
        if (batteryPercentage == null || batteryPercentage < 0 || batteryPercentage > 100) {
            throw new BadRequestException("Battery percentage must be between 0 and 100");
        }
    }

    private void validateCodeDoesNotExist(String code) {
        if (vehicleRepository.existsByCodeIgnoreCase(code)) {
            throw new BadRequestException("Vehicle code already exists: " + code);
        }
    }

    private VehicleResponse toResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .code(vehicle.getCode())
                .model(vehicle.getModel())
                .status(vehicle.getStatus())
                .batteryPercentage(vehicle.getBatteryPercentage())
                .terminalId(vehicle.getTerminalId())
                .chargingStationId(vehicle.getChargingStationId())
                .active(vehicle.getActive())
                .build();
    }
}

