package br.com.vehicle_application.application.ports;

import br.com.vehicle_application.core.domain.Sale;
import br.com.vehicle_application.core.domain.Vehicle;

import java.util.List;

public interface VehicleInboundPort {

    void createVehicle(Vehicle vehicle);

    void editVehicle(Vehicle vehicle);

    List<Vehicle> getSaleVehicles();

    void buyVehicle(Sale sale);

    List<Vehicle> getSoldVehicles();
}
