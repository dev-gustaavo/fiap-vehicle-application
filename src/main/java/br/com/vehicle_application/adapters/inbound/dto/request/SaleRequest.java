package br.com.vehicle_application.adapters.inbound.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SaleRequest {

    @NotNull(message = "Market Reference Code is required")
    @NotBlank(message = "Market Reference Code is required")
    private String marketReferenceCode;
}
