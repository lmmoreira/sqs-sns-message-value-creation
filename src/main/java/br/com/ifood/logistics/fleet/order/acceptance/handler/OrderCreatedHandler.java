package br.com.ccrs.logistics.fleet.order.acceptance.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.event.AbstractMessageHandler;
import com.ccrs.event.exceptions.InvalidMessageException;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;

import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.OrderCreatedEvent;
import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderService;

@Service("orderCreatedHandler")
public class OrderCreatedHandler extends AbstractMessageHandler<String, OrderCreatedEvent> {

    private final ObjectMapper mapper;
    private final OrderService orderService;

    public OrderCreatedHandler(final ObjectMapper mapper, final OrderService orderService) {
        this.mapper = mapper;
        this.orderService = orderService;
    }

    @Override
    public OrderCreatedEvent verifyAndConvert(final String messageBody) throws InvalidMessageException {
        return convert(messageBody);
    }

    private OrderCreatedEvent convert(final String messageBody) throws InvalidMessageException {
        try {
            return mapper.readValue(messageBody, OrderCreatedEvent.class);
        } catch (final IOException e) {
            final String errorMessage = String.format("Cannot convert messageBody to %s, error: %s",
                OrderCreatedEvent.class.getSimpleName(),
                e.getMessage());
            LOGGER.error(errorMessage);
            throw new InvalidMessageException(errorMessage);
        }
    }

    @Override
    public void process(final OrderCreatedEvent orderCreatedEvent) {
        final String orderUuid = orderCreatedEvent.getOrderUuid();
        final ZonedDateTime createdDate = orderCreatedEvent.getCreatedDate();
        orderService.updateOrderStatusToCreated(orderUuid, createdDate);
    }

    @Override
    public void notifyRetryLimitReachedError(final String message, final Exception e) {
        final String errorMessage = "Retry limit reached for message: " + message;
        LOGGER.error(errorMessage, e);
    }
}
