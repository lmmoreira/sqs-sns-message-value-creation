package br.com.ccrs.logistics.fleet.order.acceptance.service;

import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderReject;

public interface OrderRejectService {
    OrderReject createOrderRejection(String externalId, OrderReject.RejectReason reason, final String tenantIdentifier);
}
