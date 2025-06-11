package br.com.vehicle_application.adapters.outbound.db;

import br.com.vehicle_application.core.entity.VehicleEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<VehicleEntity, Integer> {

    Optional<VehicleEntity> findByMarketReferenceCode(String makertReferenceCode);

    List<VehicleEntity> findByToSellTrueAndQuantityAvailableGreaterThan(Integer quantity, Sort price);
}
