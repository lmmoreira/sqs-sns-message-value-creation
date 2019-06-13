package br.com.ccrs.logistics.fleet.order.acceptance.repository;

import br.com.ccrs.logistics.fleet.order.acceptance.model.OrderReject;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRejectRepository extends JpaRepository<OrderReject, Long> {
}
