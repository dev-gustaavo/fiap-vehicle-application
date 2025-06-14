package br.com.vehicle_application.application.usecases;

import br.com.vehicle_application.application.mappers.VehicleMapper;
import br.com.vehicle_application.application.ports.SaleOutboundPort;
import br.com.vehicle_application.application.ports.VehicleOutboundPort;
import br.com.vehicle_application.core.domain.Sale;
import br.com.vehicle_application.core.domain.Vehicle;
import br.com.vehicle_application.core.exception.VehicleException;
import br.com.vehicle_application.core.utils.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleUseCaseTest {

    @Mock
    private VehicleOutboundPort vehicleOutboundPort;

    @Mock
    private SaleOutboundPort saleOutboundPort;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleUseCase vehicleUseCase;

    private Vehicle vehicle;
    private Sale sale;
    private final String marketReferenceCode = "REF123";

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setMarketReferenceCode(marketReferenceCode);
        vehicle.setPrice(BigDecimal.valueOf(100000.0));
        vehicle.setQuantityAvailable(1);
        vehicle.setToSell(true);

        sale = new Sale();
        sale.setMarketReferenceCode(marketReferenceCode);
    }

    @Test
    void createVehicle_ShouldCreateNewVehicleWhenNotExists() {
        when(vehicleOutboundPort.getVehicleByMarketReferenceCode(vehicle)).thenReturn(vehicle);

        vehicleUseCase.createVehicle(vehicle);

        verify(vehicleOutboundPort).createVehicle(vehicle);
        assertEquals(2, vehicle.getQuantityAvailable());
    }

    @Test
    void createVehicle_ShouldIncrementQuantityWhenVehicleExists() {
        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setQuantityAvailable(5);
        when(vehicleOutboundPort.getVehicleByMarketReferenceCode(vehicle)).thenReturn(existingVehicle);

        vehicleUseCase.createVehicle(vehicle);

        verify(vehicleOutboundPort).createVehicle(existingVehicle);
        assertEquals(6, existingVehicle.getQuantityAvailable());
    }

    @Test
    void editVehicle_ShouldCallOutboundPort() {
        vehicleUseCase.editVehicle(vehicle);
        verify(vehicleOutboundPort).editVehicle(vehicle);
    }

    @Test
    void getSaleVehicles_ShouldReturnListFromOutboundPort() {
        List<Vehicle> expectedVehicles = Collections.singletonList(vehicle);
        when(vehicleOutboundPort.getSaleVehicles()).thenReturn(expectedVehicles);

        List<Vehicle> result = vehicleUseCase.getSaleVehicles();

        assertEquals(expectedVehicles, result);
    }

    @Test
    void buyVehicle_ShouldCompleteSaleWhenVehicleIsAvailable() {
        when(vehicleOutboundPort.getVehicleByMarketReferenceCode(marketReferenceCode)).thenReturn(vehicle);

        vehicleUseCase.buyVehicle(sale);

        verify(saleOutboundPort).saleVehicle(sale);
        assertEquals(0, vehicle.getQuantityAvailable());
        verify(vehicleOutboundPort).editVehicle(vehicle);
    }

    @Test
    void buyVehicle_ShouldThrowExceptionWhenVehicleNotFound() {
        when(vehicleOutboundPort.getVehicleByMarketReferenceCode(marketReferenceCode)).thenReturn(null);

        VehicleException exception = assertThrows(VehicleException.class,
                () -> vehicleUseCase.buyVehicle(sale));

        assertEquals(ErrorMessage.VEHICLE_NOT_FOUND_EXCEPTION_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void buyVehicle_ShouldThrowExceptionWhenVehicleNotForSale() {
        vehicle.setToSell(false);
        when(vehicleOutboundPort.getVehicleByMarketReferenceCode(marketReferenceCode)).thenReturn(vehicle);

        VehicleException exception = assertThrows(VehicleException.class,
                () -> vehicleUseCase.buyVehicle(sale));

        assertEquals(ErrorMessage.VEHICLE_NOT_FOR_SALE_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void buyVehicle_ShouldThrowExceptionWhenVehicleNotAvailable() {
        vehicle.setQuantityAvailable(0);
        when(vehicleOutboundPort.getVehicleByMarketReferenceCode(marketReferenceCode)).thenReturn(vehicle);

        VehicleException exception = assertThrows(VehicleException.class,
                () -> vehicleUseCase.buyVehicle(sale));

        assertEquals(ErrorMessage.VEHICLE_NOT_AVAILABLE_ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    void getSoldVehicles_ShouldReturnListFromOutboundPort() {
        List<Vehicle> expectedVehicles = Collections.singletonList(vehicle);
        when(vehicleOutboundPort.getSoldVehicles()).thenReturn(expectedVehicles);

        List<Vehicle> result = vehicleUseCase.getSoldVehicles();

        assertEquals(expectedVehicles, result);
    }
}