package cl.duoc.airflytrip.tariffs.services;

import cl.duoc.airflytrip.tariffs.clients.RouteClient;
import cl.duoc.airflytrip.tariffs.clients.response.RouteResponse;
import cl.duoc.airflytrip.tariffs.dtos.request.CreateTariffRequest;
import cl.duoc.airflytrip.tariffs.dtos.request.UpdateTariffRequest;
import cl.duoc.airflytrip.tariffs.dtos.response.TariffResponse;
import cl.duoc.airflytrip.tariffs.exceptions.BadRequestException;
import cl.duoc.airflytrip.tariffs.exceptions.NotFoundException;
import cl.duoc.airflytrip.tariffs.exceptions.RemoteServiceException;
import cl.duoc.airflytrip.tariffs.models.Tariff;
import cl.duoc.airflytrip.tariffs.repositories.TariffRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepository tariffRepository;
    private final RouteClient routeClient;

    public List<TariffResponse> findAll() {
        return tariffRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<TariffResponse> findActive() {
        return tariffRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    public TariffResponse findById(Long id) {
        return toResponse(findTariffById(id));
    }

    public List<TariffResponse> findByRouteId(Long routeId) {
        validateRouteExists(routeId);
        return tariffRepository.findByRouteId(routeId).stream().map(this::toResponse).toList();
    }

    public TariffResponse findByRouteIdAndVehicleType(Long routeId, String vehicleType) {
        Tariff tariff = tariffRepository.findByRouteIdAndVehicleTypeIgnoreCase(routeId, vehicleType)
                .orElseThrow(() -> new NotFoundException(
                        "Tariff not found for route " + routeId + " and vehicle type " + vehicleType
                ));

        return toResponse(tariff);
    }

    public TariffResponse create(CreateTariffRequest request) {
        validateRouteExists(request.getRouteId());
        validatePrices(request.getBasePrice(), request.getPricePerKm());
        validateTariffDoesNotExist(request.getRouteId(), request.getVehicleType());

        Tariff tariff = Tariff.builder()
                .routeId(request.getRouteId())
                .basePrice(request.getBasePrice())
                .pricePerKm(request.getPricePerKm())
                .vehicleType(request.getVehicleType())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        return toResponse(tariffRepository.save(tariff));
    }

    public TariffResponse update(Long id, UpdateTariffRequest request) {
        Tariff tariff = findTariffById(id);

        validateRouteExists(request.getRouteId());
        validatePrices(request.getBasePrice(), request.getPricePerKm());

        tariff.setRouteId(request.getRouteId());
        tariff.setBasePrice(request.getBasePrice());
        tariff.setPricePerKm(request.getPricePerKm());
        tariff.setVehicleType(request.getVehicleType());
        tariff.setActive(request.getActive() != null ? request.getActive() : tariff.getActive());

        return toResponse(tariffRepository.save(tariff));
    }

    public void delete(Long id) {
        Tariff tariff = findTariffById(id);
        tariff.setActive(false);
        tariffRepository.save(tariff);
    }

    private Tariff findTariffById(Long id) {
        return tariffRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tariff not found with id: " + id));
    }

    private void validateRouteExists(Long routeId) {
        try {
            RouteResponse route = routeClient.findById(routeId);

            if (route == null || Boolean.FALSE.equals(route.getActive())) {
                throw new BadRequestException("Route is not active or does not exist with id: " + routeId);
            }
        } catch (FeignException.NotFound exception) {
            throw new BadRequestException("Route not found with id: " + routeId);
        } catch (FeignException exception) {
            throw new RemoteServiceException("Error communicating with route-service");
        }
    }

    private void validatePrices(BigDecimal basePrice, BigDecimal pricePerKm) {
        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Base price must be greater than or equal to 0");
        }

        if (pricePerKm == null || pricePerKm.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Price per km must be greater than or equal to 0");
        }
    }

    private void validateTariffDoesNotExist(Long routeId, String vehicleType) {
        if (tariffRepository.existsByRouteIdAndVehicleTypeIgnoreCase(routeId, vehicleType)) {
            throw new BadRequestException("Tariff already exists for route " + routeId + " and vehicle type " + vehicleType);
        }
    }

    private TariffResponse toResponse(Tariff tariff) {
        return TariffResponse.builder()
                .id(tariff.getId())
                .routeId(tariff.getRouteId())
                .basePrice(tariff.getBasePrice())
                .pricePerKm(tariff.getPricePerKm())
                .vehicleType(tariff.getVehicleType())
                .active(tariff.getActive())
                .build();
    }
}