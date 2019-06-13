package br.com.ccrs.logistics.fleet.order.acceptance.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.event.exceptions.InvalidMessageException;

import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import br.com.ccrs.logistics.fleet.order.acceptance.config.JacksonConfiguration;
import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.OrderCreatedEvent;
import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderService;

public class OrderCreatedHandlerTest {

    private final String orderUuid = "03ade10e-534a-4a8a-9219-de6b98c42c6a";
    private OrderService service;
    private OrderCreatedHandler handler;
    private final ObjectMapper mapper = new JacksonConfiguration().configureObjectMapper().build();

    @Before
    public void before() {
        service = mock(OrderService.class);
        handler = new OrderCreatedHandler(mapper, service);
    }

    @Test
    public void shouldVerifyEventType() throws InvalidMessageException {
        handler.verifyAndConvert(convertToJson(getMessage()));
    }

    @Test(expected = InvalidMessageException.class)
    public void shouldThrowExceptionWhenNoUuid() throws InvalidMessageException {
        final Map<String, Object> message = getMessage();
        message.remove("orderUuid");
        handler.verifyAndConvert(convertToJson(message));
    }

    @Test
    public void shouldUpdateOrder() throws InvalidMessageException {
        final OrderCreatedEvent event = handler.verifyAndConvert(convertToJson(getMessage()));
        handler.process(event);

        //verify(service, times(1)).updateOrderStatusToCreated(orderUuid, event.getCreatedDate());
    }

    private Map<String, Object> getMessage() {
        final Map<String, Object> map = new HashMap<>();
        map.put("orderUuid", orderUuid);
        map.put("externalId", "exId-1234");
        map.put("trackableSourceName", "tSN-test");
        map.put("regionUuid", "316d8cb8-df35-43b8-bab9-cc249946eadb");
        map.put("regionName", "test region");
        map.put("subRegionUuid", "84c084de-3a89-4789-9455-d2b5dfa003cd");
        map.put("subRegionName", "test sub region");
        map.put("createdDate", ZonedDateTime.now());
        return map;
    }

    private String convertToJson(final Map<String, Object> event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
