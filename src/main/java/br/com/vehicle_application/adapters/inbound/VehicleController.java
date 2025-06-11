package br.com.vehicle_application.adapters.inbound;

import br.com.vehicle_application.adapters.inbound.dto.request.SaleRequest;
import br.com.vehicle_application.adapters.inbound.dto.request.VehicleRequest;
import br.com.vehicle_application.application.mappers.SaleMapper;
import br.com.vehicle_application.application.mappers.VehicleMapper;
import br.com.vehicle_application.application.ports.VehicleInboundPort;
import br.com.vehicle_application.core.domain.Vehicle;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleInboundPort vehicleInboundPort;
    private final VehicleMapper vehicleMapper;
    private final SaleMapper saleMapper;

    @PostMapping
    public ResponseEntity createVehicle(
            @RequestBody @Valid VehicleRequest vehicleRequest) {
        var vehicle = vehicleMapper.toDomain(vehicleRequest);
        vehicleInboundPort.createVehicle(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity editVehicle(@RequestBody VehicleRequest vehicleRequest) {
        var vehicle = vehicleMapper.toDomain(vehicleRequest);
        vehicleInboundPort.editVehicle(vehicle);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sales")
    public ResponseEntity<List<Vehicle>> getSaleVehicles() {
        var saleVehicles = vehicleInboundPort.getSaleVehicles();
        return ResponseEntity.ok(saleVehicles);
    }

    @PostMapping("/buy")
    public ResponseEntity buyVehicle(@RequestBody @Valid SaleRequest saleRequest,
                                     @AuthenticationPrincipal Jwt jwt) {
        var userId = jwt.getSubject();
        var sale = saleMapper.toDomain(saleRequest, userId);
        vehicleInboundPort.buyVehicle(sale);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/sold")
    public ResponseEntity<List<Vehicle>> getSoldVehicles() {
        var saledVehicles = vehicleInboundPort.getSoldVehicles();
        return ResponseEntity.ok(saledVehicles);
    }
}
