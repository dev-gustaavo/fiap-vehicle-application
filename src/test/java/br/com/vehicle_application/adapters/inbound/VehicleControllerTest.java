package br.com.vehicle_application.adapters.inbound;

import br.com.vehicle_application.adapters.inbound.dto.request.SaleRequest;
import br.com.vehicle_application.adapters.inbound.dto.request.VehicleRequest;
import br.com.vehicle_application.application.mappers.SaleMapper;
import br.com.vehicle_application.application.mappers.VehicleMapper;
import br.com.vehicle_application.application.ports.VehicleInboundPort;
import br.com.vehicle_application.core.domain.Sale;
import br.com.vehicle_application.core.domain.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleInboundPort vehicleInboundPort;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private SaleMapper saleMapper;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private VehicleController vehicleController;

    private VehicleRequest vehicleRequest;
    private SaleRequest saleRequest;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicleRequest = new VehicleRequest();
        saleRequest = new SaleRequest();
        vehicle = new Vehicle();
    }

    @Test
    void createVehicle_ShouldReturnCreatedStatus() {
        when(vehicleMapper.toDomain(vehicleRequest)).thenReturn(vehicle);

        ResponseEntity<?> response = vehicleController.createVehicle(vehicleRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(vehicleInboundPort).createVehicle(vehicle);
    }

    @Test
    void editVehicle_ShouldReturnOkStatus() {
        when(vehicleMapper.toDomain(vehicleRequest)).thenReturn(vehicle);

        ResponseEntity<?> response = vehicleController.editVehicle(vehicleRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(vehicleInboundPort).editVehicle(vehicle);
    }

    @Test
    void getSaleVehicles_ShouldReturnListOfVehicles() {
        List<Vehicle> expectedVehicles = Collections.singletonList(vehicle);
        when(vehicleInboundPort.getSaleVehicles()).thenReturn(expectedVehicles);

        ResponseEntity<List<Vehicle>> response = vehicleController.getSaleVehicles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedVehicles, response.getBody());
    }

    @Test
    void buyVehicle_ShouldReturnCreatedStatus() {
        String userId = "user123";
        when(jwt.getSubject()).thenReturn(userId);

        var vehicle = new Vehicle()
                .setBrand("brand")
                .setColor("color")
                .setFactoryYear("2025")
                .setId(1)
                .setModel("model")
                .setPrice(BigDecimal.TEN)
                .setMarketReferenceCode("123")
                .setModelYear("2025")
                .setQuantityAvailable(1)
                .setToSell(true);
        var sale = new Sale()
                .setVehicle(vehicle)
                .setBuyerId("123")
                .setPrice(vehicle.getPrice())
                .setMarketReferenceCode("123");
        when(saleMapper.toDomain(any(), eq(userId)))
                .thenReturn(sale);

        ResponseEntity<?> response = vehicleController.buyVehicle(saleRequest, jwt);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(vehicleInboundPort).buyVehicle(sale);
    }

    @Test
    void getSoldVehicles_ShouldReturnListOfVehicles() {
        List<Vehicle> expectedVehicles = Collections.singletonList(vehicle);
        when(vehicleInboundPort.getSoldVehicles()).thenReturn(expectedVehicles);

        ResponseEntity<List<Vehicle>> response = vehicleController.getSoldVehicles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedVehicles, response.getBody());
    }

    @Test
    void buyVehicle_ShouldUseJwtSubjectAsUserId() {
        String expectedUserId = "user123";
        when(jwt.getSubject()).thenReturn(expectedUserId);
        when(saleMapper.toDomain(any(), eq(expectedUserId))).thenReturn(new Sale());
        vehicleController.buyVehicle(saleRequest, jwt);
        verify(saleMapper).toDomain(any(), eq(expectedUserId));
    }
}