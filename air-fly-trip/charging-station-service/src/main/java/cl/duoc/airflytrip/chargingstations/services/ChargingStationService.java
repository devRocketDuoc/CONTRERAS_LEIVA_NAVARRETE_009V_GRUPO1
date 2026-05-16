package cl.duoc.airflytrip.chargingstations.services;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.duoc.airflytrip.chargingstations.clients.TerminalClient;
import cl.duoc.airflytrip.chargingstations.clients.response.TerminalResponse;
import cl.duoc.airflytrip.chargingstations.dtos.request.CreateChargingStationRequest;
import cl.duoc.airflytrip.chargingstations.dtos.request.UpdateChargingStationRequest;
import cl.duoc.airflytrip.chargingstations.dtos.request.UpdateChargingStationStatusRequest;
import cl.duoc.airflytrip.chargingstations.dtos.response.ChargingStationResponse;
import cl.duoc.airflytrip.chargingstations.exceptions.BadRequestException;
import cl.duoc.airflytrip.chargingstations.exceptions.NotFoundException;
import cl.duoc.airflytrip.chargingstations.exceptions.RemoteServiceException;
import cl.duoc.airflytrip.chargingstations.models.ChargingStation;
import cl.duoc.airflytrip.chargingstations.repositories.ChargingStationRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChargingStationService {

    private static final String DEFAULT_STATUS = "AVAILABLE";

    private final ChargingStationRepository chargingStationRepository;
    private final TerminalClient terminalClient;

    public List<ChargingStationResponse> findAll() {
        return chargingStationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ChargingStationResponse> findActive() {
        return chargingStationRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ChargingStationResponse findById(Long id) {
        ChargingStation station = findChargingStationById(id);
        return toResponse(station);
    }

    public List<ChargingStationResponse> findByTerminalId(Long terminalId) {
        validateTerminalExists(terminalId);

        return chargingStationRepository.findByTerminalId(terminalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ChargingStationResponse create(CreateChargingStationRequest request) {
        validateTerminalExists(request.getTerminalId());
        validateCapacity(request.getCapacity(), request.getAvailableSlots());
        validateCodeDoesNotExist(request.getCode());

        ChargingStation station = ChargingStation.builder()
                .code(request.getCode())
                .name(request.getName())
                .terminalId(request.getTerminalId())
                .capacity(request.getCapacity())
                .availableSlots(request.getAvailableSlots())
                .status(request.getStatus() != null ? request.getStatus() : DEFAULT_STATUS)
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        ChargingStation savedStation = chargingStationRepository.save(station);

        return toResponse(savedStation);
    }

    public ChargingStationResponse update(Long id, UpdateChargingStationRequest request) {
        ChargingStation station = findChargingStationById(id);

        validateTerminalExists(request.getTerminalId());
        validateCapacity(request.getCapacity(), request.getAvailableSlots());

        station.setCode(request.getCode());
        station.setName(request.getName());
        station.setTerminalId(request.getTerminalId());
        station.setCapacity(request.getCapacity());
        station.setAvailableSlots(request.getAvailableSlots());
        station.setStatus(request.getStatus() != null ? request.getStatus() : station.getStatus());
        station.setActive(request.getActive() != null ? request.getActive() : station.getActive());

        ChargingStation updatedStation = chargingStationRepository.save(station);

        return toResponse(updatedStation);
    }

    public ChargingStationResponse updateStatus(Long id, UpdateChargingStationStatusRequest request) {
        ChargingStation station = findChargingStationById(id);
        station.setStatus(request.getStatus());

        ChargingStation updatedStation = chargingStationRepository.save(station);

        return toResponse(updatedStation);
    }

    public void delete(Long id) {
        ChargingStation station = findChargingStationById(id);
        station.setActive(false);
        chargingStationRepository.save(station);
    }

    private ChargingStation findChargingStationById(Long id) {
        return chargingStationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Charging station not found with id: " + id));
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

    private void validateCapacity(Integer capacity, Integer availableSlots) {
        if (capacity == null || availableSlots == null) {
            throw new BadRequestException("Capacity and available slots are required");
        }

        if (availableSlots > capacity) {
            throw new BadRequestException("Available slots cannot be greater than capacity");
        }
    }

    private void validateCodeDoesNotExist(String code) {
        if (chargingStationRepository.existsByCodeIgnoreCase(code)) {
            throw new BadRequestException("Charging station code already exists: " + code);
        }
    }

    private ChargingStationResponse toResponse(ChargingStation station) {
        return ChargingStationResponse.builder()
                .id(station.getId())
                .code(station.getCode())
                .name(station.getName())
                .terminalId(station.getTerminalId())
                .capacity(station.getCapacity())
                .availableSlots(station.getAvailableSlots())
                .status(station.getStatus())
                .active(station.getActive())
                .build();
    }
}
