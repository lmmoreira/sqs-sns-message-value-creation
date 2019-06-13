package br.com.ccrs.logistics.fleet.order.acceptance.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.event.AbstractMessageHandler;
import com.ccrs.event.exceptions.InvalidMessageException;
import com.ccrs.event.exceptions.RecoverableException;

import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.Event;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;

@Service("saturatedRegionHandler")
public class SaturatedRegionHandler extends AbstractMessageHandler<String, Event> {

    private static final String REGION_UUID = "REGION_UUID";

    private final ObjectMapper jacksonObjectMapper;
    private final RegionService regionService;

    @Autowired
    public SaturatedRegionHandler(final ObjectMapper jacksonObjectMapper, final RegionService regionService) {
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.regionService = regionService;
    }

    @Override
    public Event verifyAndConvert(final String message) throws InvalidMessageException {
        return verify(convert(message));
    }

    private Event verify(final Event event) throws InvalidMessageException {
        if (!isNormalized(event) && !isSaturated(event)) {
            final String errorMessage = "EventType '" + event.getEventType() + "' is invalid for this handler.";
            LOGGER.error(errorMessage);
            throw new InvalidMessageException(errorMessage);
        }
        return event;
    }

    private Event convert(final String message) throws InvalidMessageException {
        try {
            return jacksonObjectMapper.readValue(message, Event.class);
        } catch (final IOException e) {
            LOGGER.error("Cannot convert message to Event, error: {}", e.getMessage());
            throw new InvalidMessageException(e.getMessage());
        }
    }

    @Override
    public void process(final Event event) throws RecoverableException {
        try {
            final String regionUuid = event.getParameter(REGION_UUID);
            if (isNormalized(event)) {
                LOGGER.debug("Normalizing region {}", regionUuid);
                regionService.updateSaturatedByRegion(regionUuid, false);
            } else {
                LOGGER.debug("Saturating region {}", regionUuid);
                regionService.updateSaturatedByRegion(regionUuid, true);
            }
        } catch (final Exception e) {
            throw new RecoverableException("Retrying saturated region message: " + event, e);
        }
    }

    private boolean isSaturated(final Event event) {
        return Event.EventType.SATURATED_REGION.equals(event.getEventType());
    }

    private boolean isNormalized(final Event event) {
        return Event.EventType.NORMALIZED_REGION.equals(event.getEventType());
    }

    @Override
    public void notifyRetryLimitReachedError(final String message, final Exception e) {
        LOGGER.warn("Retry limit for {}", message);
    }
}
