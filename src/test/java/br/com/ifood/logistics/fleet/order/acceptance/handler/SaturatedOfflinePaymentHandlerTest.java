package br.com.ccrs.logistics.fleet.order.acceptance.handler;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.event.exceptions.InvalidMessageException;
import com.ccrs.event.exceptions.RecoverableException;

import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.Event;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;

public class SaturatedOfflinePaymentHandlerTest {

    private final String regionUuid = "03ade10e-534a-4a8a-9219-de6b98c42c6a";
    private RegionService service;
    private SaturatedOfflinePaymentHandler handler;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void before() {
        service = mock(RegionService.class);
        handler = new SaturatedOfflinePaymentHandler(mapper, service);
    }

    @Test(expected = InvalidMessageException.class)
    public void shouldVerifyEventType() throws InvalidMessageException {
        handler.verifyAndConvert(getMessageSaturated());
        handler.verifyAndConvert(getMessageWrongEventType());
    }

    @Test(expected = InvalidMessageException.class)
    public void shouldNotConvert() throws InvalidMessageException {
        handler.verifyAndConvert(getMessageWithInvalidPayload());
    }

    @Test
    public void shouldNormalize() throws InvalidMessageException, RecoverableException {
        final Event event = handler.verifyAndConvert(getMessageNormalized());
        handler.process(event);

        verify(service, times(1)).normalizeOfflinePaymentByRegion(regionUuid);
        verify(service, never()).saturateOfflinePaymentByRegion(anyString());
    }

    @Test
    public void shouldSaturate() throws InvalidMessageException, RecoverableException {
        final Event event = handler.verifyAndConvert(getMessageSaturated());
        handler.process(event);

        verify(service, times(1)).saturateOfflinePaymentByRegion(regionUuid);
        verify(service, never()).normalizeOfflinePaymentByRegion(anyString());
    }

    private String getMessageWrongEventType() {
        final Map<String, Object> event =
            Map.of("eventType", "PRE_SHIFT_BEGIN", "parameters", Map.of("SHIFT", "any-shift", "ZONE", "any-zone"));
        return convertToJson(event);
    }

    private String getMessageWithInvalidPayload() {
        final Map<String, Object> event =
            Map.of("unknown-field", "PRE_SHIFT_BEGIN", "parameters", Map.of("SHIFT", "any-shift", "ZONE", "any-zone"));
        return convertToJson(event);
    }

    private String getMessageNormalized() {
        final Map<String, Object> event = Map.of("eventType",
            "NORMALIZED_OFFLINE_REGION",
            "parameters",
            Map.of("REGION_ID", "52", "REGION_UUID", regionUuid));
        return convertToJson(event);
    }

    private String getMessageSaturated() {
        final Map<String, Object> event = Map.of("eventType",
            "SATURATED_OFFLINE_REGION",
            "parameters",
            Map.of("REGION_ID", "52", "REGION_UUID", regionUuid, "ORDER_COUNT", "0", "WORKER_COUNT", "0"));
        return convertToJson(event);
    }

    private String convertToJson(final Map<String, Object> event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
