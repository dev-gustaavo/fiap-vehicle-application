package br.com.vehicle_application.adapters.outbound;

import br.com.vehicle_application.adapters.outbound.db.SaleRepository;
import br.com.vehicle_application.adapters.outbound.db.VehicleRepository;
import br.com.vehicle_application.application.mappers.VehicleMapper;
import br.com.vehicle_application.core.domain.Vehicle;
import br.com.vehicle_application.core.entity.SaleEntity;
import br.com.vehicle_application.core.entity.VehicleEntity;
import br.com.vehicle_application.core.exception.VehicleException;
import br.com.vehicle_application.core.utils.ErrorMessage;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleGatewayTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleGateway vehicleGateway;

    private Vehicle vehicle;
    private VehicleEntity vehicleEntity;
    private final String marketReferenceCode = "REF123";

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setMarketReferenceCode(marketReferenceCode);

        vehicleEntity = new VehicleEntity();
        vehicleEntity.setMarketReferenceCode(marketReferenceCode);
    }

    @Test
    void createVehicle_ShouldSaveVehicleSuccessfully() {
        when(vehicleMapper.toDbEntity(vehicle)).thenReturn(vehicleEntity);
        when(vehicleRepository.save(vehicleEntity)).thenReturn(vehicleEntity);

        vehicleGateway.createVehicle(vehicle);

        verify(vehicleMapper).toDbEntity(vehicle);
        verify(vehicleRepository).save(vehicleEntity);
    }

    @Test
    void createVehicle_ShouldThrowVehicleExceptionOnFailure() {
        when(vehicleMapper.toDbEntity(vehicle)).thenThrow(new RuntimeException("DB error"));

        VehicleException exception = assertThrows(VehicleException.class,
                () -> vehicleGateway.createVehicle(vehicle));

        assertEquals(ErrorMessage.VEHICLE_CREATE_ERROR_MESSAGE, exception.getMessage());
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void editVehicle_ShouldUpdateExistingVehicle() {
        when(vehicleRepository.findByMarketReferenceCode(marketReferenceCode))
                .thenReturn(Optional.of(vehicleEntity));
        when(vehicleMapper.toDbEntity(vehicle)).thenReturn(vehicleEntity);
        when(vehicleRepository.save(vehicleEntity)).thenReturn(vehicleEntity);

        vehicleGateway.editVehicle(vehicle);

        verify(vehicleRepository).findByMarketReferenceCode(marketReferenceCode);
        verify(vehicleMapper).toDbEntity(vehicle);
        verify(vehicleRepository).save(vehicleEntity);
    }

    @Test
    void editVehicle_ShouldThrowEntityNotFoundExceptionWhenVehicleNotFound() {
        when(vehicleRepository.findByMarketReferenceCode(marketReferenceCode))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> vehicleGateway.editVehicle(vehicle));

        assertEquals(ErrorMessage.VEHICLE_NOT_FOUND_EXCEPTION_ERROR_MESSAGE, exception.getMessage());
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void editVehicle_ShouldThrowRuntimeExceptionOnGeneralError() {
        when(vehicleRepository.findByMarketReferenceCode(marketReferenceCode))
                .thenReturn(Optional.of(vehicleEntity));
        when(vehicleMapper.toDbEntity(vehicle)).thenThrow(new RuntimeException("Mapping error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> vehicleGateway.editVehicle(vehicle));

        assertEquals(ErrorMessage.VEHICLE_EDIT_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void getSaleVehicles_ShouldReturnSortedVehicles() {
        List<VehicleEntity> entities = Collections.singletonList(vehicleEntity);
        List<Vehicle> expectedVehicles = Collections.singletonList(vehicle);

        when(vehicleRepository.findByToSellTrueAndQuantityAvailableGreaterThan(0,
                Sort.by(Sort.Direction.ASC, "price"))).thenReturn(entities);
        when(vehicleMapper.toVehicleList(entities)).thenReturn(expectedVehicles);

        List<Vehicle> result = vehicleGateway.getSaleVehicles();

        assertEquals(expectedVehicles, result);
        verify(vehicleRepository).findByToSellTrueAndQuantityAvailableGreaterThan(0,
                Sort.by(Sort.Direction.ASC, "price"));
    }

    @Test
    void getVehicleByMarketReferenceCode_WithVehicleParameter_ShouldReturnMappedVehicle() {
        when(vehicleRepository.findByMarketReferenceCode(marketReferenceCode))
                .thenReturn(Optional.of(vehicleEntity));
        when(vehicleMapper.fromDbEntity(vehicleEntity)).thenReturn(vehicle);

        Vehicle result = vehicleGateway.getVehicleByMarketReferenceCode(vehicle);

        assertEquals(vehicle, result);
    }

    @Test
    void getVehicleByMarketReferenceCode_WithVehicleParameter_ShouldReturnOriginalWhenNotFound() {
        when(vehicleRepository.findByMarketReferenceCode(marketReferenceCode))
                .thenReturn(Optional.empty());

        Vehicle result = vehicleGateway.getVehicleByMarketReferenceCode(vehicle);

        assertEquals(vehicle, result);
    }

    @Test
    void getVehicleByMarketReferenceCode_WithStringParameter_ShouldReturnMappedVehicle() {
        when(vehicleRepository.findByMarketReferenceCode(marketReferenceCode))
                .thenReturn(Optional.of(vehicleEntity));
        when(vehicleMapper.fromDbEntity(vehicleEntity)).thenReturn(vehicle);

        Vehicle result = vehicleGateway.getVehicleByMarketReferenceCode(marketReferenceCode);

        assertEquals(vehicle, result);
    }

    @Test
    void getVehicleByMarketReferenceCode_WithStringParameter_ShouldReturnNullWhenNotFound() {
        when(vehicleRepository.findByMarketReferenceCode(marketReferenceCode))
                .thenReturn(Optional.empty());

        Vehicle result = vehicleGateway.getVehicleByMarketReferenceCode(marketReferenceCode);

        assertNull(result);
    }

    @Test
    void getSoldVehicles_ShouldReturnMappedVehiclesFromSales() {
        SaleEntity saleEntity = new SaleEntity();
        saleEntity.setVehicle(vehicleEntity);
        List<SaleEntity> sales = Collections.singletonList(saleEntity);
        List<Vehicle> expectedVehicles = Collections.singletonList(vehicle);

        when(saleRepository.findAll()).thenReturn(sales);
        when(vehicleMapper.toVehicleList(List.of(vehicleEntity))).thenReturn(expectedVehicles);

        List<Vehicle> result = vehicleGateway.getSoldVehicles();

        assertEquals(expectedVehicles, result);
        verify(saleRepository).findAll();
    }

    @Test
    void getSaleVehicles_ShouldThrowRuntimeExceptionOnError() {
        when(vehicleRepository.findByToSellTrueAndQuantityAvailableGreaterThan(anyInt(), any(Sort.class)))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> vehicleGateway.getSaleVehicles());

        assertEquals(ErrorMessage.VEHICLE_EDIT_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void getVehicleByMarketReferenceCode_WithVehicleParameter_ShouldThrowVehicleExceptionOnError() {
        when(vehicleRepository.findByMarketReferenceCode(anyString()))
                .thenThrow(new RuntimeException("Database error"));

        VehicleException exception = assertThrows(VehicleException.class,
                () -> vehicleGateway.getVehicleByMarketReferenceCode(vehicle));

        assertEquals(ErrorMessage.VEHICLE_GET_BY_MARKET_REFERENCE_CODE_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void getVehicleByMarketReferenceCode_WithStringParameter_ShouldThrowVehicleExceptionOnError() {
        when(vehicleRepository.findByMarketReferenceCode(anyString()))
                .thenThrow(new RuntimeException("Database error"));

        VehicleException exception = assertThrows(VehicleException.class,
                () -> vehicleGateway.getVehicleByMarketReferenceCode(marketReferenceCode));

        assertEquals(ErrorMessage.VEHICLE_GET_BY_MARKET_REFERENCE_CODE_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void getSoldVehicles_ShouldThrowRuntimeExceptionOnError() {
        when(saleRepository.findAll())
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> vehicleGateway.getSoldVehicles());

        assertEquals(ErrorMessage.VEHICLE_EDIT_ERROR_MESSAGE, exception.getMessage());
    }
}