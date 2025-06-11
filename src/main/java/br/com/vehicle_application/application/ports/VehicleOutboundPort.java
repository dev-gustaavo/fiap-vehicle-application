package br.com.vehicle_application.application.ports;

import br.com.vehicle_application.core.domain.Vehicle;

import java.util.List;

public interface VehicleOutboundPort {
    void createVehicle(Vehicle vehicle);

    void editVehicle(Vehicle vehicle);

    List<Vehicle> getSaleVehicles();

    Vehicle getVehicleByMarketReferenceCode(Vehicle vehicle);

    Vehicle getVehicleByMarketReferenceCode(String marketReferenceCode);

    List<Vehicle> getSoldVehicles();
}
