package br.com.ccrs.logistics.fleet.order.acceptance.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.event.exceptions.InvalidMessageException;
import com.ccrs.event.exceptions.RecoverableException;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Optional;

import br.com.ccrs.logistics.fleet.order.acceptance.config.JacksonConfiguration;
import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.RegionChangeEvent;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;

public class RegionChangeHandlerTest {

    private final String regionUuid = "03ade10e-534a-4a8a-9219-de6b98c42c6a";
    private RegionService service;
    private RegionChangeHandler handler;
    private final ObjectMapper mapper = new JacksonConfiguration().configureObjectMapper().build();

    @Before
    public void before() {
        service = mock(RegionService.class);
        when(service.findByRegionUuid(regionUuid)).thenReturn(Optional.of(new Region()));

        handler = new RegionChangeHandler(mapper, service);
    }

    @Test
    public void shouldVerifyEventType() throws InvalidMessageException {
        handler.verifyAndConvert(getMessage());
    }

    @Test
    public void shouldUpdateRegion() throws InvalidMessageException, RecoverableException {
        final RegionChangeEvent event = handler.verifyAndConvert(getMessage());
        handler.process(event);

        verify(service, times(1)).updateRegion(any(Region.class));
        verify(service, never()).disableOfflinePaymentByRegion(anyString());
    }

    private String getMessage() {
        final var event = Map.of("uuid",
            regionUuid,
            "name",
            "Sao Paulo",
            "attributes",
            Map.of("OFFLINE_PAYMENT_ENABLED", "true", "KML_FILE_ID", "1234"),
            "lastModifiedDate",
            "2019-05-06T13:51:18.033-03:00");
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
