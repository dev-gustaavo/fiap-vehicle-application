package br.com.vehicle_application.adapters.inbound.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class VehicleRequest {

    @NotNull(message = "Brand is required")
    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Model is required")
    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Model Year is required")
    @NotBlank(message = "Model Year is required")
    private String modelYear;

    @NotNull(message = "Factory Year is required")
    @NotBlank(message = "Factory Year is required")
    private String factoryYear;

    @NotNull(message = "Color is required")
    @NotBlank(message = "Color is required")
    private String color;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Market Reference Code is required")
    @NotBlank(message = "Market Reference Code is required")
    private String marketReferenceCode;

    @NotNull(message = "To Sell is required")
    private boolean toSell;
}
