package br.com.ccrs.logistics.fleet.order.acceptance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderReject;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.OrderRejectRepository;

@Service
public class OrderRejectServiceImpl implements OrderRejectService {

    private final OrderRejectRepository orderRejectRepository;

    @Autowired
    public OrderRejectServiceImpl(final OrderRejectRepository orderRejectRepository) {
        this.orderRejectRepository = orderRejectRepository;
    }

    @Transactional
    public OrderReject createOrderRejection(final String externalId, final OrderReject.RejectReason reason,
                                            final String tenantIdentifier) {
        final OrderReject orderReject = new OrderReject();
        orderReject.setExternalId(externalId);
        orderReject.setReason(reason);
        orderReject.setTenantIdentifier(tenantIdentifier);
        return orderRejectRepository.save(orderReject);
    }

}