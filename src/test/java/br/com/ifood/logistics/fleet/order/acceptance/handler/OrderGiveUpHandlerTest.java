package br.com.ccrs.logistics.fleet.order.acceptance.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.event.exceptions.InvalidMessageException;
import com.ccrs.event.exceptions.RecoverableException;

import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.Event;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import br.com.ccrs.logistics.fleet.order.acceptance.config.JacksonConfiguration;
import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.OrderCreatedEvent;
import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderService;

public class OrderGiveUpHandlerTest {

    private final String orderUuid = "03ade10e-534a-4a8a-9219-de6b98c42c6a";
    private OrderService service;
    private OrderGiveUpHandler handler;
    private final ObjectMapper mapper = new JacksonConfiguration().configureObjectMapper().build();

    @Before
    public void before() {
        service = mock(OrderService.class);
        handler = new OrderGiveUpHandler(mapper, service);
    }

    @Test
    public void shouldVerifyEventType() throws InvalidMessageException {
        handler.verifyAndConvert(convertToJson(getMessage()));
    }

    @Test(expected = InvalidMessageException.class)
    public void shouldThrowExceptionWhenNoUuid() throws InvalidMessageException {
        final Map<String, Object> message = getInvalidMessage();
        handler.verifyAndConvert(convertToJson(message));
    }

    @Test
    public void shouldUpdateOrder() throws InvalidMessageException, RecoverableException {
        final Event event = handler.verifyAndConvert(convertToJson(getMessage()));
        handler.process(event);

        //verify(service, times(1)).giveUpOrder(orderUuid);
    }

    private Map<String, Object> getMessage() {
        final Map<String, Object> event = Map.of("eventType",
                "ORDER_STATE_CHANGE",
                "parameters",
                Map.of("ORDER_ID", "52", "ORDER_EXTERNAL_ID", "52", "ORDER_UUID", orderUuid, "CURRENT_ORDER_STATE", "GIVEN_UP"));
        return event;
    }

    private Map<String, Object> getInvalidMessage() {
        final Map<String, Object> event = Map.of("eventType",
                "ORDER_STATE_CHANGE",
                "parameters",
                Map.of("ORDER_ID", "52", "CURRENT_ORDER_STATE", "GIVEN_UP"));
        return event;
    }

    private String convertToJson(final Map<String, Object> event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
