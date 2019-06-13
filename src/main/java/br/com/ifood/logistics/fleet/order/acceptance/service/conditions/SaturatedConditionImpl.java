package br.com.ccrs.logistics.fleet.order.acceptance.service.conditions;

import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderReject;
import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderRejectService;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;

@Component
@Order(2)
public class SaturatedConditionImpl extends RejectCondition {

    @Autowired
    public SaturatedConditionImpl(final OrderRejectService orderRejectServiceImpl, final RegionService regionServiceImpl) {
        super(orderRejectServiceImpl, regionServiceImpl);
    }

    @Override
    public boolean applyRejectCondition(final ConditionDTO conditionDTO) {
        if ((conditionDTO.getOrderType().hasProperty(OrderProperty.CAN_ACCEPT_ORIGIN_WITH_DEFAULT_REGION)) && (conditionDTO.getOrigin() == null)) {
            return false;
        }

        return (conditionDTO.getOrigin().getSaturated());
    }

    @Override
    OrderReject.RejectReason getRejectReason() {
        return OrderReject.RejectReason.SATURATED_REGION;
    }

}