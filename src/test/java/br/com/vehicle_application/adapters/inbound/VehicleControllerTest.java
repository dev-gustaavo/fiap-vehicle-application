package br.com.vehicle_application.adapters.inbound;

import br.com.vehicle_application.adapters.inbound.dto.request.SaleRequest;
import br.com.vehicle_application.adapters.inbound.dto.request.VehicleRequest;
import br.com.vehicle_application.application.mappers.SaleMapper;
import br.com.vehicle_application.application.mappers.VehicleMapper;
import br.com.vehicle_application.application.ports.VehicleInboundPort;
import br.com.vehicle_application.core.domain.Sale;
import br.com.vehicle_application.core.domain.Vehicle;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleInboundPort vehicleInboundPort;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private VehicleController vehicleController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createVehicle_ShouldReturnCreated_WhenVehicleIsValid() throws Exception {
        // Arrange
        VehicleRequest vehicleRequest = createValidVehicleRequest();
        Vehicle vehicle = createValidVehicle();

        when(vehicleMapper.toDomain(any())).thenReturn(vehicle);
        doNothing().when(vehicleInboundPort).createVehicle(any());

        // Act & Assert
        mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isCreated());

        verify(vehicleMapper).toDomain(any());
        verify(vehicleInboundPort).createVehicle(any());
    }

    @Test
    void editVehicle_ShouldReturnOk_WhenVehicleIsValid() throws Exception {
        // Arrange
        VehicleRequest vehicleRequest = createValidVehicleRequest();
        Vehicle vehicle = createValidVehicle();

        when(vehicleMapper.toDomain(any(VehicleRequest.class))).thenReturn(vehicle);
        doNothing().when(vehicleInboundPort).editVehicle(any(Vehicle.class));

        // Act & Assert
        mockMvc.perform(put("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isOk());

        verify(vehicleMapper).toDomain(vehicleRequest);
        verify(vehicleInboundPort).editVehicle(vehicle);
    }

    @Test
    void getSaleVehicles_ShouldReturnVehicleList_WhenCalled() throws Exception {
        // Arrange
        List<Vehicle> vehicles = Arrays.asList(createValidVehicle(), createValidVehicle());
        when(vehicleInboundPort.getSaleVehicles()).thenReturn(vehicles);

        // Act & Assert
        mockMvc.perform(get("/vehicle/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(vehicleInboundPort).getSaleVehicles();
    }

    @Test
    void buyVehicle_ShouldReturnCreated_WhenSaleIsValid() throws Exception {
        // Arrange
        SaleRequest saleRequest = createValidSaleRequest();
        Sale sale = createValidSale();
        Jwt jwt = createMockJwt();

        when(saleMapper.toDomain(any(), anyString())).thenReturn(sale);
        doNothing().when(vehicleInboundPort).buyVehicle(any());

        // Act & Assert
        mockMvc.perform(post("/vehicle/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saleRequest))
                        .requestAttr("jwt", jwt))
                .andExpect(status().isCreated());

        verify(saleMapper).toDomain(any(), anyString());
        verify(vehicleInboundPort).buyVehicle(any());
    }

    @Test
    void getSoldVehicles_ShouldReturnVehicleList_WhenCalled() throws Exception {
        // Arrange
        List<Vehicle> soldVehicles = Arrays.asList(createValidVehicle());
        when(vehicleInboundPort.getSoldVehicles()).thenReturn(soldVehicles);

        // Act & Assert
        mockMvc.perform(get("/vehicle/sold"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(vehicleInboundPort).getSoldVehicles();
    }

    @Test
    void createVehicle_ShouldReturnBadRequest_WhenVehicleRequestIsInvalid() throws Exception {
        // Arrange
        VehicleRequest invalidRequest = new VehicleRequest();

        // Act & Assert
        mockMvc.perform(post("/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(vehicleMapper, never()).toDomain(any());
        verify(vehicleInboundPort, never()).createVehicle(any());
    }

    @Test
    void buyVehicle_ShouldReturnBadRequest_WhenSaleRequestIsInvalid() throws Exception {
        // Arrange
        SaleRequest invalidRequest = new SaleRequest();

        // Act & Assert
        mockMvc.perform(post("/vehicle/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(saleMapper, never()).toDomain(any(), anyString());
        verify(vehicleInboundPort, never()).buyVehicle(any());
    }

    private VehicleRequest createValidVehicleRequest() {
        return new VehicleRequest()
                .setBrand("Toyota")
                .setModel("Corolla")
                .setModelYear("2022")
                .setFactoryYear("2022")
                .setColor("Branco")
                .setPrice(new BigDecimal("85000.00"))
                .setMarketReferenceCode("TOY-COR-2022")
                .setToSell(true);
    }

    private Vehicle createValidVehicle() {
        return new Vehicle()
                .setId(1)
                .setBrand("Toyota")
                .setModel("Corolla")
                .setModelYear("2022")
                .setFactoryYear("2022")
                .setColor("Branco")
                .setPrice(new BigDecimal("85000.00"))
                .setMarketReferenceCode("TOY-COR-2022")
                .setToSell(true)
                .setQuantityAvailable(5);
    }

    private SaleRequest createValidSaleRequest() {
        return new SaleRequest()
                .setMarketReferenceCode("TOY-COR-2022");
    }

    private Sale createValidSale() {
        return new Sale()
                .setMarketReferenceCode("TOY-COR-2022")
                .setBuyerId("user123")
                .setPrice(new BigDecimal("85000.00"))
                .setVehicle(createValidVehicle());
    }

    private Jwt createMockJwt() {
        return mock(Jwt.class);
    }
}