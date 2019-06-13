package br.com.ccrs.logistics.fleet.order.acceptance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import br.com.ccrs.logistics.fleet.order.acceptance.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOneByOrderUuid(String orderUuid);

    Optional<Order> findOneByExternalIdAndTenantIdentifier(String externalId, String tenantIdentifier);
}

