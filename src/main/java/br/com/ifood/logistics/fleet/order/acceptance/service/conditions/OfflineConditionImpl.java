package br.com.ccrs.logistics.fleet.order.acceptance.service.conditions;

import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderRejectService;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderProperty;
import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderReject;

@Component
@Order(4)
public class OfflineConditionImpl extends RejectCondition {

    @Autowired
    public OfflineConditionImpl(final OrderRejectService orderRejectServiceImpl, final RegionService regionServiceImpl) {
        super(orderRejectServiceImpl, regionServiceImpl);
    }

    @Override
    public boolean applyRejectCondition(final ConditionDTO conditionDTO) {
        if ((conditionDTO.getOrderType().hasProperty(OrderProperty.NEEDS_PAYMENT_TYPE_CHECK)) && (conditionDTO.getPaymentType().isPaymentAtDelivery())) {
            return !conditionDTO.getOrigin().getOfflinePaymentEnabled();
        }

        return false;
    }

    @Override
    OrderReject.RejectReason getRejectReason() {
        return OrderReject.RejectReason.PAYMENT_OFFLINE;
    }

}