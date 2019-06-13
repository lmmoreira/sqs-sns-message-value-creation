package br.com.ccrs.logistics.fleet.order.acceptance.service.conditions;

import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.OrderDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderReject;
import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderRejectService;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;

public abstract class RejectCondition {

    private final OrderRejectService orderRejectService;
    protected RegionService regionService;

    public RejectCondition(final OrderRejectService orderRejectService, final RegionService regionService) {
        this.orderRejectService = orderRejectService;
        this.regionService = regionService;
    }

    public boolean acceptance(final ConditionDTO conditionDTO) {
        if (applyRejectCondition(conditionDTO)) {
            final OrderDTO orderDTO = conditionDTO.getOrderDTO();
            orderRejectService
                    .createOrderRejection(orderDTO.getExternalId(), getRejectReason(), orderDTO.getTenantIdentifier());
            return false;
        }

        return true;
    }

    abstract boolean applyRejectCondition(ConditionDTO conditionDTO);

    abstract OrderReject.RejectReason getRejectReason();

}
