package br.com.ccrs.logistics.fleet.order.acceptance.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.event.AbstractMessageHandler;
import com.ccrs.event.exceptions.InvalidMessageException;
import com.ccrs.event.exceptions.RecoverableException;

import org.springframework.stereotype.Service;

import java.io.IOException;

import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.Event;
import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderService;

@Service("orderGiveUpHandler")
public class OrderGiveUpHandler extends AbstractMessageHandler<String, Event> {

    private static final String ORDER_UUID = "ORDER_UUID";
    private static final String ORDER_STATE_TYPE = "CURRENT_ORDER_STATE";
    private static final String ORDER_STATE_TYPE_GIVE_UP = "GIVEN_UP";

    private final ObjectMapper jacksonObjectMapper;
    private final OrderService orderService;

    public OrderGiveUpHandler(final ObjectMapper jacksonObjectMapper, final OrderService orderService) {
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.orderService = orderService;
    }

    @Override
    public Event verifyAndConvert(final String message) throws InvalidMessageException {
        LOGGER.debug("Verifying and converting message: {}", message);
        return verify(convert(message));
    }

    @Override
    public void process(final Event event)  throws RecoverableException  {
        try {
            final String eventType = event.getParameter(ORDER_STATE_TYPE);
            final String orderUuid = event.getParameter(ORDER_UUID);

            if (isGiveUp(eventType)) {
                orderService.giveUpOrder(orderUuid);
            }
        } catch (final Exception e) {
            throw new RecoverableException("Retrying order state change message: " + event, e);
        }
    }

    @Override
    public void notifyRetryLimitReachedError(final String message, final Exception e) {
        final String errorMessage = "Retry limit reached for message: " + message;
        LOGGER.error(errorMessage, e);
    }

    private Event convert(final String message) throws InvalidMessageException {
        try {
            return jacksonObjectMapper.readValue(message, Event.class);
        } catch (final IOException e) {
            LOGGER.error("Cannot convert message to Event, error: {}", e.getMessage());
            throw new InvalidMessageException(e.getMessage());
        }
    }

    private Event verify(final Event event) throws InvalidMessageException {
        if (!isOrderStateChange(event)) {
            final String errorMessage = "EventType '" + event.getEventType() + "' is invalid for this handler.";
            LOGGER.error(errorMessage);
            throw new InvalidMessageException(errorMessage);
        }
        return event;
    }

    private boolean isOrderStateChange(final Event event) {
        return Event.EventType.ORDER_STATE_CHANGE.equals(event.getEventType());
    }

    private boolean isGiveUp(final String type) {
        return ORDER_STATE_TYPE_GIVE_UP.equals(type);
    }

}
