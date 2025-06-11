package br.com.vehicle_application.adapters.outbound;

import br.com.vehicle_application.adapters.outbound.db.SaleRepository;
import br.com.vehicle_application.adapters.outbound.db.VehicleRepository;
import br.com.vehicle_application.application.mappers.VehicleMapper;
import br.com.vehicle_application.application.ports.VehicleOutboundPort;
import br.com.vehicle_application.core.domain.Vehicle;
import br.com.vehicle_application.core.entity.SaleEntity;
import br.com.vehicle_application.core.exception.VehicleException;
import br.com.vehicle_application.core.utils.ErrorMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VehicleGateway implements VehicleOutboundPort {

    private final VehicleRepository vehicleRepository;
    private final SaleRepository saleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public void createVehicle(Vehicle vehicle) {
        try {
            var vehicleDbEntity = vehicleMapper.toDbEntity(vehicle);
            vehicleRepository.save(vehicleDbEntity);
        } catch (Exception exception) {
            throw new VehicleException(ErrorMessage.VEHICLE_CREATE_ERROR_MESSAGE);
        }
    }

    @Override
    public void editVehicle(Vehicle vehicle) {
        try {
            var vehicleEntity = vehicleRepository
                    .findByMarketReferenceCode(vehicle.getMarketReferenceCode());

            if (vehicleEntity.isEmpty())
                throw new EntityNotFoundException();

            var vehicleDbEntity = vehicleEntity.get();
            vehicle.setId(vehicleDbEntity.getId());
            vehicleDbEntity = vehicleMapper.toDbEntity(vehicle);

            vehicleRepository.save(vehicleDbEntity);
        } catch (EntityNotFoundException entityNotFoundException) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND_EXCEPTION_ERROR_MESSAGE);
        } catch (Exception exception) {
            throw new RuntimeException(ErrorMessage.VEHICLE_EDIT_ERROR_MESSAGE);
        }
    }

    @Override
    public List<Vehicle> getSaleVehicles() {
        try {
            var vehiclesEntities = vehicleRepository.findByToSellTrueAndQuantityAvailableGreaterThan(
                    0, Sort.by(Sort.Direction.ASC, "price")
            );

            return vehicleMapper.toVehicleList(vehiclesEntities);
        } catch (Exception exception) {
            throw new RuntimeException(ErrorMessage.VEHICLE_EDIT_ERROR_MESSAGE);
        }
    }

    @Override
    public Vehicle getVehicleByMarketReferenceCode(Vehicle vehicle) {
        try {
            var vehicleEntity = vehicleRepository.findByMarketReferenceCode(vehicle.getMarketReferenceCode())
                    .orElse(null);

            if (vehicleEntity == null)
                return vehicle;

            return vehicleMapper.fromDbEntity(vehicleEntity);
        } catch (Exception exception) {
            throw new VehicleException(ErrorMessage.VEHICLE_GET_BY_MARKET_REFERENCE_CODE_ERROR_MESSAGE);
        }
    }

    @Override
    public Vehicle getVehicleByMarketReferenceCode(String marketReferenceCode) {
        try {
            var vehicleEntity = vehicleRepository.findByMarketReferenceCode(marketReferenceCode)
                    .orElse(null);

            if (vehicleEntity == null)
                return null;

            return vehicleMapper.fromDbEntity(vehicleEntity);
        } catch (Exception exception) {
            throw new VehicleException(ErrorMessage.VEHICLE_GET_BY_MARKET_REFERENCE_CODE_ERROR_MESSAGE);
        }
    }

    @Override
    public List<Vehicle> getSoldVehicles() {
        try {
            var sales = saleRepository.findAll();
            var vehiclesEntities = sales.stream()
                    .map(SaleEntity::getVehicle).toList();

            return vehicleMapper.toVehicleList(vehiclesEntities);
        } catch (Exception exception) {
            throw new RuntimeException(ErrorMessage.VEHICLE_EDIT_ERROR_MESSAGE);
        }
    }
}
