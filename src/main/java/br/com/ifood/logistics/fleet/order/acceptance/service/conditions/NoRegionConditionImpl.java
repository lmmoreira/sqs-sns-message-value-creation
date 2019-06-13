package br.com.ccrs.logistics.fleet.order.acceptance.service.conditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderProperty;
import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderReject;
import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderRejectService;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;

@Component
@Order(1)
public class NoRegionConditionImpl extends RejectCondition {

    @Autowired
    public NoRegionConditionImpl(final OrderRejectService orderRejectServiceImpl, final RegionService regionServiceImpl) {
        super(orderRejectServiceImpl, regionServiceImpl);
    }

    @Override
    public boolean applyRejectCondition(final ConditionDTO conditionDTO) {
        if ((conditionDTO.getOrderType().hasProperty(OrderProperty.CAN_ACCEPT_ORIGIN_WITH_DEFAULT_REGION)) && (conditionDTO.getOrigin() == null)) {
            return false;
        }

        return conditionDTO.getOrigin() == null;
    }

    @Override
    OrderReject.RejectReason getRejectReason() {
        return OrderReject.RejectReason.NO_REGION;
    }

}