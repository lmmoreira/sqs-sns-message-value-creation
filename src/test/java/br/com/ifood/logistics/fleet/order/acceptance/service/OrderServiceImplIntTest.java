package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import com.deliverypf.gis.sdk.PolygonSdkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.event.AbstractTopicSender;
import com.ccrs.event.exceptions.InvalidMessageException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

import br.com.ccrs.logistics.fleet.order.acceptance.AbstractIntTest;
import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.LocalityDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.OrderDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.model.PaymentType;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.OrderRepository;

public class OrderServiceImplIntTest extends AbstractIntTest {

    private static final String API_KEY_ATTRIBUTE = "api-key";
    private static final String TENANT_ATTRIBUTE = "tenant-identifier";
    private static final String ORDER_UUID_ATTRIBUTE = "order-uuid";

    private static final String COUNTRY_CODE_BR = "BR";
    private static final String TRACKABLE_SOURCE_NAME = "ccrs";

    @Autowired
    private OrderService orderService;

    @Autowired
    private AbstractTopicSender topicSender;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private PolygonSdkService polygonSdkService;

    @Captor
    private ArgumentCaptor<Map<String, String>> attributesCaptor;

    @Captor
    private ArgumentCaptor<String> bodyCaptor;

    @Value("${sns.order.create.queue.name}")
    private String topicName;

    @Before
    public void setup() {
        // orderRepository.deleteAll();
    }

    @Test
    public void testNotificationAfterCreation() throws InvalidMessageException, JsonProcessingException {
        final String apiKey = UUID.randomUUID().toString();
        final OrderDTO orderDTO = getOrderDTO();
        final String rawOrder = objectMapper.writeValueAsString(orderDTO);
        final String uuid = orderService.createOrder(COUNTRY_CODE_BR, rawOrder, apiKey, orderDTO);

        verify(topicSender).sendMessage(eq(topicName), bodyCaptor.capture(), anyBoolean(), attributesCaptor.capture());

        assertEquals(rawOrder, bodyCaptor.getValue());
        assertEquals(COUNTRY_CODE_BR.toLowerCase(), attributesCaptor.getValue().get(TENANT_ATTRIBUTE));
        assertEquals(apiKey, attributesCaptor.getValue().get(API_KEY_ATTRIBUTE));
        assertEquals(uuid, attributesCaptor.getValue().get(ORDER_UUID_ATTRIBUTE));
    }

    @Test
    public void testSameUuidWhenOrderIsAlreadyCreated() throws JsonProcessingException {
        final String apiKey = UUID.randomUUID().toString();
        final OrderDTO orderDTO = getOrderDTO();
        final String rawOrder = objectMapper.writeValueAsString(orderDTO);
        final String uuid = orderService.createOrder(COUNTRY_CODE_BR, rawOrder, apiKey, orderDTO);

        assertEquals(uuid, orderService.createOrder(COUNTRY_CODE_BR, rawOrder, apiKey, orderDTO));
    }

    @Test
    public void testNotificationEvenWhenOrderIsAlreadyCreated() throws InvalidMessageException,
                                                                JsonProcessingException {
        final String apiKey = UUID.randomUUID().toString();
        final OrderDTO orderDTO = getOrderDTO();
        final String rawOrder = objectMapper.writeValueAsString(orderDTO);
        final String uuid = orderService.createOrder(COUNTRY_CODE_BR, rawOrder, apiKey, orderDTO);

        reset(topicSender);

        orderService.createOrder(COUNTRY_CODE_BR, rawOrder, apiKey, orderDTO);
        verify(topicSender).sendMessage(eq(topicName), bodyCaptor.capture(), anyBoolean(), attributesCaptor.capture());

        assertEquals(rawOrder, bodyCaptor.getValue());
        assertEquals(COUNTRY_CODE_BR.toLowerCase(), attributesCaptor.getValue().get(TENANT_ATTRIBUTE));
        assertEquals(apiKey, attributesCaptor.getValue().get(API_KEY_ATTRIBUTE));
        assertEquals(uuid, attributesCaptor.getValue().get(ORDER_UUID_ATTRIBUTE));
    }

    private OrderDTO getOrderDTO() {
        return new OrderDTO(TRACKABLE_SOURCE_NAME, "external", ZonedDateTime.now(), getOrigin(), getDestination(),
            PaymentType.CARD, 10L, 10L, "MARKET_PLACE", Currency.getInstance("AUD"));
    }

    private LocalityDTO getOrigin() {
        return emptyLocalityDTO(1.0);
    }

    private LocalityDTO emptyLocalityDTO(final double v) {
        return new LocalityDTO(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, v, v,
            ZonedDateTime.now());
    }

    private LocalityDTO getDestination() {
        return emptyLocalityDTO(2.0);
    }
}
