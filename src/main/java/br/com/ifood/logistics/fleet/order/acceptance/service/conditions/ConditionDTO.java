package br.com.ccrs.logistics.fleet.order.acceptance.service.conditions;

import java.util.Optional;

import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.OrderDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderType;
import br.com.ccrs.logistics.fleet.order.acceptance.model.PaymentType;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConditionDTO {
    private OrderDTO orderDTO;
    private Region origin;

    public OrderType getOrderType() {
        return Optional.ofNullable(orderDTO.getOrderType()).map(OrderType::valueOf).orElse(OrderType.OWN_DELIVERY);
    }

    public PaymentType getPaymentType() {
        return orderDTO.getPaymentType();
    }

}
