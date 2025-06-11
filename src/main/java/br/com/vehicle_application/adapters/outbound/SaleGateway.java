package br.com.vehicle_application.adapters.outbound;

import br.com.vehicle_application.adapters.outbound.db.SaleRepository;
import br.com.vehicle_application.application.mappers.SaleMapper;
import br.com.vehicle_application.application.ports.SaleOutboundPort;
import br.com.vehicle_application.core.domain.Sale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaleGateway implements SaleOutboundPort {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;

    @Override
    public void saleVehicle(Sale sale) {
        try {
            var saleEntity = saleMapper.toDbEntity(sale);
            saleRepository.save(saleEntity);
        } catch (Exception exception) {
            throw new RuntimeException("Error when trying to sale vehicle."
                    + " Please try again later.");
        }
    }
}
