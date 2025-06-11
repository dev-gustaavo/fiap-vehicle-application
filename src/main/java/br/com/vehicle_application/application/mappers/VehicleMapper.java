package br.com.vehicle_application.application.mappers;

import br.com.vehicle_application.adapters.inbound.dto.request.VehicleRequest;
import br.com.vehicle_application.core.domain.Vehicle;
import br.com.vehicle_application.core.entity.VehicleEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    VehicleEntity toDbEntity(Vehicle vehicle);

    Vehicle toDomain(VehicleRequest vehicleRequest);

    List<Vehicle> toVehicleList(List<VehicleEntity> vehicleEntities);

    Vehicle fromDbEntity(VehicleEntity vehicleEntity);
}
