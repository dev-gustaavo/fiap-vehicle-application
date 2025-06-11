package br.com.vehicle_application.application.mappers;

import br.com.vehicle_application.adapters.inbound.dto.request.SaleRequest;
import br.com.vehicle_application.core.domain.Sale;
import br.com.vehicle_application.core.entity.SaleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    SaleEntity toDbEntity(Sale sale);

    @Mapping(target = "buyerId", source = "userId")
    Sale toDomain(SaleRequest saleRequest, String userId);

}
