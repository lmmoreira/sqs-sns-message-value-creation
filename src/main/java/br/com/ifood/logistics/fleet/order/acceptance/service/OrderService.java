package br.com.ccrs.logistics.fleet.order.acceptance.service;

import java.time.ZonedDateTime;

import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.OrderDTO;

public interface OrderService {

    boolean acceptance(OrderDTO orderDTO);

    void updateOrderStatusToCreated(String orderUuid, final ZonedDateTime createdDate);

    String createOrder(String countryCode, String rawOrder, String apiKey, OrderDTO order);

    void giveUpOrder(String orderUUid);

}
