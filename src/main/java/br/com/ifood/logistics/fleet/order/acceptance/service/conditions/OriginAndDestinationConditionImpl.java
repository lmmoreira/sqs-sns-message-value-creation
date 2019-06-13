package br.com.ccrs.logistics.fleet.order.acceptance.service.conditions;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderProperty;
import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderReject;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;
import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderRejectService;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class OriginAndDestinationConditionImpl extends RejectCondition {

    @Autowired
    public OriginAndDestinationConditionImpl(final OrderRejectService orderRejectServiceImpl, final RegionService regionServiceImpl) {
        super(orderRejectServiceImpl, regionServiceImpl);
    }

    @Override
    public boolean applyRejectCondition(final ConditionDTO conditionDTO) {
        if (conditionDTO.getOrderType().hasProperty(OrderProperty.NEEDS_DESTINATION_REGION_CHECK)) {
            return isOriginDifferentDestination(
                                conditionDTO.getOrigin(),
                                regionService.findByLongitudeLatitude(
                                            conditionDTO.getOrderDTO().getDestination().getLongitude(),
                                            conditionDTO.getOrderDTO().getDestination().getLatitude()));
        }

        return false;
    }

    @Override
    OrderReject.RejectReason getRejectReason() {
        return OrderReject.RejectReason.REGION_MISMATCH;
    }

    private boolean isOriginDifferentDestination(final Region origin, final Region destination) {
        return (!origin.equals(destination));
    }

}
