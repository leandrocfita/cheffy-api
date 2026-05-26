package br.com.fiap.cheffy.infrastructure.adapters.in.mappers;

import br.com.fiap.cheffy.application.order.ports.in.records.OrderStatusCommandRecord;
import br.com.fiap.cheffy.infrastructure.adapters.in.records.InputOrderStatusRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InputOrderStatusMapper {

    OrderStatusCommandRecord toCommand(InputOrderStatusRecord input);
}
