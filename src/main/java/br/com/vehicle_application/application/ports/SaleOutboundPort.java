package br.com.vehicle_application.application.ports;

import br.com.vehicle_application.core.domain.Sale;

public interface SaleOutboundPort {
    void saleVehicle(Sale sale);
}
