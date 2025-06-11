package br.com.vehicle_application.application.usecases;

import br.com.vehicle_application.application.mappers.VehicleMapper;
import br.com.vehicle_application.application.ports.SaleOutboundPort;
import br.com.vehicle_application.application.ports.VehicleInboundPort;
import br.com.vehicle_application.application.ports.VehicleOutboundPort;
import br.com.vehicle_application.core.domain.Sale;
import br.com.vehicle_application.core.domain.Vehicle;
import br.com.vehicle_application.core.exception.VehicleException;
import br.com.vehicle_application.core.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleUseCase implements VehicleInboundPort {

    private final VehicleOutboundPort vehicleOutboundPort;
    private final SaleOutboundPort saleOutboundPort;
    private final VehicleMapper vehicleMapper;

    @Override
    public void createVehicle(Vehicle vehicle) {

        var vehicleDomain = vehicleOutboundPort
                .getVehicleByMarketReferenceCode(vehicle);

        setVehicleAvailableQuantity(vehicleDomain);

        vehicleOutboundPort.createVehicle(vehicleDomain);
    }

    private void setVehicleAvailableQuantity(Vehicle vehicleDomain) {
        var quantityAvailable = vehicleDomain.getQuantityAvailable() == 0 ? 1 : vehicleDomain.getQuantityAvailable() + 1;

        vehicleDomain.setQuantityAvailable(quantityAvailable);
    }

    @Override
    public void editVehicle(Vehicle vehicle) {
        vehicleOutboundPort.editVehicle(vehicle);
    }

    @Override
    public List<Vehicle> getSaleVehicles() {
        return vehicleOutboundPort.getSaleVehicles();
    }

    @Override
    public void buyVehicle(Sale sale) {
        var vehicleDomain = vehicleOutboundPort.getVehicleByMarketReferenceCode(sale.getMarketReferenceCode());

        if (vehicleDomain == null)
            throw new VehicleException(ErrorMessage.VEHICLE_NOT_FOUND_EXCEPTION_ERROR_MESSAGE);

        if (!vehicleDomain.isToSell())
            throw new VehicleException(ErrorMessage.VEHICLE_NOT_FOR_SALE_ERROR_MESSAGE);

        if (vehicleDomain.getQuantityAvailable() <= 0)
            throw new VehicleException(ErrorMessage.VEHICLE_NOT_AVAILABLE_ERROR_MESSAGE);

        sale.setVehicle(vehicleDomain);
        sale.setPrice(vehicleDomain.getPrice());

        saleOutboundPort.saleVehicle(sale);

        vehicleDomain.setQuantityAvailable(vehicleDomain.getQuantityAvailable() - 1);
        vehicleOutboundPort.editVehicle(vehicleDomain);
    }

    @Override
    public List<Vehicle> getSoldVehicles() {
        return vehicleOutboundPort.getSoldVehicles();
    }
}
