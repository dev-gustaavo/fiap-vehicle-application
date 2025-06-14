package br.com.vehicle_application.application.mappers;

import br.com.vehicle_application.adapters.inbound.dto.request.SaleRequest;
import br.com.vehicle_application.core.domain.Sale;
import br.com.vehicle_application.core.domain.Vehicle;
import br.com.vehicle_application.core.entity.SaleEntity;
import br.com.vehicle_application.core.entity.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SaleMapperImplTest {

    private final SaleMapperImpl saleMapper = new SaleMapperImpl();

    @Test
    void toDbEntity_ShouldReturnNullWhenSaleIsNull() {
        SaleEntity result = saleMapper.toDbEntity(null);
        assertNull(result);
    }

    @Test
    void toDbEntity_ShouldMapAllFieldsCorrectly() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setModelYear("2022");
        vehicle.setFactoryYear("2021");
        vehicle.setColor("Black");
        vehicle.setPrice(BigDecimal.valueOf(120000.0));
        vehicle.setMarketReferenceCode("TOY2022COR");
        vehicle.setToSell(true);
        vehicle.setQuantityAvailable(5);

        Sale sale = new Sale();
        sale.setPrice(BigDecimal.valueOf(115000.0));
        sale.setVehicle(vehicle);
        sale.setBuyerId("user123");

        // Act
        SaleEntity result = saleMapper.toDbEntity(sale);

        // Assert
        assertNotNull(result);
        assertEquals(sale.getPrice(), result.getPrice());
        assertEquals(sale.getBuyerId(), result.getBuyerId());

        VehicleEntity vehicleEntity = result.getVehicle();
        assertNotNull(vehicleEntity);
        assertEquals(vehicle.getId(), vehicleEntity.getId());
        assertEquals(vehicle.getBrand(), vehicleEntity.getBrand());
        assertEquals(vehicle.getModel(), vehicleEntity.getModel());
        assertEquals(vehicle.getModelYear(), vehicleEntity.getModelYear());
        assertEquals(vehicle.getFactoryYear(), vehicleEntity.getFactoryYear());
        assertEquals(vehicle.getColor(), vehicleEntity.getColor());
        assertEquals(vehicle.getPrice(), vehicleEntity.getPrice());
        assertEquals(vehicle.getMarketReferenceCode(), vehicleEntity.getMarketReferenceCode());
        assertEquals(vehicle.isToSell(), vehicleEntity.isToSell());
        assertEquals(vehicle.getQuantityAvailable(), vehicleEntity.getQuantityAvailable());
    }

    @Test
    void toDbEntity_ShouldHandleNullVehicle() {
        // Arrange
        Sale sale = new Sale();
        sale.setPrice(BigDecimal.valueOf(115000.0));
        sale.setVehicle(null);
        sale.setBuyerId("user123");

        // Act
        SaleEntity result = saleMapper.toDbEntity(sale);

        // Assert
        assertNotNull(result);
        assertEquals(sale.getPrice(), result.getPrice());
        assertEquals(sale.getBuyerId(), result.getBuyerId());
        assertNull(result.getVehicle());
    }

    @Test
    void toDomain_ShouldReturnNullWhenBothInputsAreNull() {
        Sale result = saleMapper.toDomain(null, null);
        assertNull(result);
    }

    @Test
    void toDomain_ShouldCreateSaleFromRequestAndUserId() {
        // Arrange
        SaleRequest request = new SaleRequest();
        request.setMarketReferenceCode("TOY2022COR");
        String userId = "user123";

        // Act
        Sale result = saleMapper.toDomain(request, userId);

        // Assert
        assertNotNull(result);
        assertEquals(request.getMarketReferenceCode(), result.getMarketReferenceCode());
        assertEquals(userId, result.getBuyerId());
    }

    @Test
    void toDomain_ShouldHandleNullRequest() {
        // Arrange
        String userId = "user123";

        // Act
        Sale result = saleMapper.toDomain(null, userId);

        // Assert
        assertNotNull(result);
        assertNull(result.getMarketReferenceCode());
        assertEquals(userId, result.getBuyerId());
    }

    @Test
    void toDomain_ShouldHandleNullUserId() {
        // Arrange
        SaleRequest request = new SaleRequest();
        request.setMarketReferenceCode("TOY2022COR");

        // Act
        Sale result = saleMapper.toDomain(request, null);

        // Assert
        assertNotNull(result);
        assertEquals(request.getMarketReferenceCode(), result.getMarketReferenceCode());
        assertNull(result.getBuyerId());
    }

    @Test
    void vehicleToVehicleEntity_ShouldReturnNullWhenVehicleIsNull() {
        VehicleEntity result = saleMapper.vehicleToVehicleEntity(null);
        assertNull(result);
    }

    @Test
    void vehicleToVehicleEntity_ShouldMapAllFieldsCorrectly() {
        // Arrange
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setBrand("Toyota");
        vehicle.setModel("Corolla");
        vehicle.setModelYear("2022");
        vehicle.setFactoryYear("2021");
        vehicle.setColor("Black");
        vehicle.setPrice(BigDecimal.valueOf(120000.0));
        vehicle.setMarketReferenceCode("TOY2022COR");
        vehicle.setToSell(true);
        vehicle.setQuantityAvailable(5);

        // Act
        VehicleEntity result = saleMapper.vehicleToVehicleEntity(vehicle);

        // Assert
        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals(vehicle.getBrand(), result.getBrand());
        assertEquals(vehicle.getModel(), result.getModel());
        assertEquals(vehicle.getModelYear(), result.getModelYear());
        assertEquals(vehicle.getFactoryYear(), result.getFactoryYear());
        assertEquals(vehicle.getColor(), result.getColor());
        assertEquals(vehicle.getPrice(), result.getPrice());
        assertEquals(vehicle.getMarketReferenceCode(), result.getMarketReferenceCode());
        assertEquals(vehicle.isToSell(), result.isToSell());
        assertEquals(vehicle.getQuantityAvailable(), result.getQuantityAvailable());
    }
}