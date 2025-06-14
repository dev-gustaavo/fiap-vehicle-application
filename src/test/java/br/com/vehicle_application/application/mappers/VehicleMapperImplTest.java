package br.com.vehicle_application.application.mappers;

import br.com.vehicle_application.adapters.inbound.dto.request.VehicleRequest;
import br.com.vehicle_application.core.domain.Vehicle;
import br.com.vehicle_application.core.entity.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VehicleMapperImplTest {

    private final VehicleMapperImpl mapper = new VehicleMapperImpl();

    @Test
    void toDbEntity_ShouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toDbEntity(null));
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

        // Act
        VehicleEntity result = mapper.toDbEntity(vehicle);

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

    @Test
    void toDomain_ShouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDomain_ShouldMapAllFieldsFromRequest() {
        // Arrange
        VehicleRequest request = new VehicleRequest();
        request.setBrand("Toyota");
        request.setModel("Corolla");
        request.setModelYear("2022");
        request.setFactoryYear("2021");
        request.setColor("Black");
        request.setPrice(BigDecimal.valueOf(120000.0));
        request.setMarketReferenceCode("TOY2022COR");
        request.setToSell(true);

        // Act
        Vehicle result = mapper.toDomain(request);

        // Assert
        assertNotNull(result);
        assertEquals(request.getBrand(), result.getBrand());
        assertEquals(request.getModel(), result.getModel());
        assertEquals(request.getModelYear(), result.getModelYear());
        assertEquals(request.getFactoryYear(), result.getFactoryYear());
        assertEquals(request.getColor(), result.getColor());
        assertEquals(request.getPrice(), result.getPrice());
        assertEquals(request.getMarketReferenceCode(), result.getMarketReferenceCode());
        assertEquals(request.isToSell(), result.isToSell());
        assertNull(result.getId()); // ID não vem do request
        assertEquals(0, result.getQuantityAvailable()); // QuantityAvailable não vem do request
    }

    @Test
    void toVehicleList_ShouldReturnNullWhenInputIsNull() {
        assertNull(mapper.toVehicleList(null));
    }

    @Test
    void toVehicleList_ShouldReturnEmptyListWhenInputIsEmpty() {
        List<VehicleEntity> emptyList = new ArrayList<>();
        List<Vehicle> result = mapper.toVehicleList(emptyList);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toVehicleList_ShouldMapAllEntitiesInList() {
        // Arrange
        VehicleEntity entity1 = new VehicleEntity();
        entity1.setId(1);
        entity1.setBrand("Toyota");

        VehicleEntity entity2 = new VehicleEntity();
        entity2.setId(2);
        entity2.setBrand("Honda");

        List<VehicleEntity> entities = List.of(entity1, entity2);

        // Act
        List<Vehicle> result = mapper.toVehicleList(entities);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity1.getBrand(), result.get(0).getBrand());
        assertEquals(entity2.getId(), result.get(1).getId());
        assertEquals(entity2.getBrand(), result.get(1).getBrand());
    }

    @Test
    void fromDbEntity_ShouldReturnNullWhenInputIsNull() {
        assertNull(mapper.fromDbEntity(null));
    }

    @Test
    void fromDbEntity_ShouldMapAllFields() {
        // Arrange
        VehicleEntity entity = new VehicleEntity();
        entity.setId(1);
        entity.setBrand("Toyota");
        entity.setModel("Corolla");
        entity.setModelYear("2022");
        entity.setFactoryYear("2021");
        entity.setColor("Black");
        entity.setPrice(BigDecimal.valueOf(120000.0));
        entity.setMarketReferenceCode("TOY2022COR");
        entity.setToSell(true);
        entity.setQuantityAvailable(5);

        // Act
        Vehicle result = mapper.fromDbEntity(entity);

        // Assert
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getBrand(), result.getBrand());
        assertEquals(entity.getModel(), result.getModel());
        assertEquals(entity.getModelYear(), result.getModelYear());
        assertEquals(entity.getFactoryYear(), result.getFactoryYear());
        assertEquals(entity.getColor(), result.getColor());
        assertEquals(entity.getPrice(), result.getPrice());
        assertEquals(entity.getMarketReferenceCode(), result.getMarketReferenceCode());
        assertEquals(entity.isToSell(), result.isToSell());
        assertEquals(entity.getQuantityAvailable(), result.getQuantityAvailable());
    }

    @Test
    void fromDbEntity_ShouldHandleNullQuantityAvailable() {
        // Arrange
        VehicleEntity entity = new VehicleEntity();
        entity.setId(1);
        entity.setQuantityAvailable(null);

        // Act
        Vehicle result = mapper.fromDbEntity(entity);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getQuantityAvailable());
    }
}