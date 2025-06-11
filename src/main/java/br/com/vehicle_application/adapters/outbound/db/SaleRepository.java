package br.com.vehicle_application.adapters.outbound.db;

import br.com.vehicle_application.core.entity.SaleEntity;
import br.com.vehicle_application.core.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepository extends JpaRepository<SaleEntity, Long> {

    @Query("SELECT s.vehicle " +
            "FROM SaleEntity s " +
            "ORDER BY s.vehicle.price ASC")
    List<VehicleEntity> findAllSoldVehicles();
}
