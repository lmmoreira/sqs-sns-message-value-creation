package br.com.ccrs.logistics.fleet.order.acceptance.handler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.event.exceptions.InvalidMessageException;
import com.ccrs.event.exceptions.RecoverableException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.Event;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;

public class SaturatedRegionHandlerTest {

    private SaturatedRegionHandler saturatedRegionHandler;
    private ObjectMapper jacksonObjectMapper;
    private RegionService regionService;

    @Before
    public void before() {
        jacksonObjectMapper = new ObjectMapper();
        regionService = mock(RegionService.class);
        saturatedRegionHandler = new SaturatedRegionHandler(jacksonObjectMapper, regionService);
    }

    @Test
    public void verifyAndConvertSaturatedRegionTest() throws InvalidMessageException {
        final Event event = saturatedRegionHandler.verifyAndConvert(getSaturatedEventMessage());
        Assert.assertEquals(Event.EventType.SATURATED_REGION, event.getEventType());
        Assert.assertEquals("03ade10e-534a-4a8a-9219-de6b98c42c6a", event.getParameter("REGION_UUID"));
    }

    @Test
    public void verifyAndConvertNormalizedRegionTest() throws InvalidMessageException {
        final Event event = saturatedRegionHandler.verifyAndConvert(getNormalizedEventMessage());
        Assert.assertEquals(Event.EventType.NORMALIZED_REGION, event.getEventType());
        Assert.assertEquals("03ade10e-534a-4a8a-9219-de6b98c42c6a", event.getParameter("REGION_UUID"));
    }

    @Test
    public void processSaturatedRegionTest() throws RecoverableException {
        saturatedRegionHandler.process(getSaturatedEventObject());
        verify(regionService, times(1)).updateSaturatedByRegion("03ade10e-534a-4a8a-9219-de6b98c42c6a", true);
    }

    @Test
    public void processNormalizedRegionTest() throws RecoverableException {
        saturatedRegionHandler.process(getNormalizedEventObject());
        verify(regionService, times(1)).updateSaturatedByRegion("03ade10e-534a-4a8a-9219-de6b98c42c6a", false);
    }

    private String getSaturatedEventMessage() {
        return "{\"eventType\": \"SATURATED_REGION\",\"parameters\":{\"REGION_ID\":\"52\",\"REGION_UUID\": \"03ade10e-534a-4a8a-9219-de6b98c42c6a\", \"ORDER_COUNT\": \"0\", \"WORKER_COUNT\": \"0\"}}";
    }

    private Event getSaturatedEventObject() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("REGION_ID", "52");
        parameters.put("REGION_UUID", "03ade10e-534a-4a8a-9219-de6b98c42c6a");
        parameters.put("ORDER_COUNT", "0");
        parameters.put("WORKER_COUNT", "0");
        return new Event(Event.EventType.SATURATED_REGION, parameters);
    }

    private String getNormalizedEventMessage() {
        return "{\"eventType\": \"NORMALIZED_REGION\",\"parameters\":{\"REGION_ID\":\"52\",\"REGION_UUID\": \"03ade10e-534a-4a8a-9219-de6b98c42c6a\"}}";
    }

    private Event getNormalizedEventObject() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("REGION_ID", "52");
        parameters.put("REGION_UUID", "03ade10e-534a-4a8a-9219-de6b98c42c6a");
        return new Event(Event.EventType.NORMALIZED_REGION, parameters);
    }

}
