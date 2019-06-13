package br.com.ccrs.logistics.fleet.order.acceptance.handler;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.event.AbstractMessageHandler;
import com.ccrs.event.exceptions.InvalidMessageException;
import com.ccrs.event.exceptions.RecoverableException;

import br.com.ccrs.logistics.fleet.order.acceptance.exception.RegionNotFoundException;
import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.RegionChangeEvent;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;

@Service("regionChangeHandler")
public class RegionChangeHandler extends AbstractMessageHandler<String, RegionChangeEvent> {

    private static final String OFFLINE_PAYMENT_ENABLED = "OFFLINE_PAYMENT_ENABLED";
    private static final String KML_FILE_ID = "KML_FILE_ID";


    private final ObjectMapper mapper;
    private final RegionService regionService;

    public RegionChangeHandler(final ObjectMapper mapper, final RegionService regionService) {
        this.mapper = mapper;
        this.regionService = regionService;
    }

    @Override
    public RegionChangeEvent verifyAndConvert(final String message) throws InvalidMessageException {
        LOGGER.debug("Converting message: {}", message);
        return convert(message);
    }

    private RegionChangeEvent convert(final String message) throws InvalidMessageException {
        try {
            return mapper.readValue(message, RegionChangeEvent.class);
        } catch (final IOException e) {
            LOGGER.error("Cannot convert message to Event, error: {}", e.getMessage());
            throw new InvalidMessageException(e.getMessage());
        }
    }

    @Override
    public void process(final RegionChangeEvent event) throws RecoverableException {
        try {
            final String regionUuid = event.getRegionUuid();
            final String name = event.getName();
            final String kmlId = event.getAttributes().get(KML_FILE_ID);
            final String offlinePaymentEnabled = event.getAttributes().get(OFFLINE_PAYMENT_ENABLED);

            final Optional<Region> optionalRegion = regionService.findByRegionUuid(regionUuid);
            final Region region = optionalRegion.orElseGet(Region::new);
            region.setRegionUuid(regionUuid);
            region.setName(name);
            region.setKml(kmlId);
            region.setOfflinePaymentEnabled(Boolean.valueOf(offlinePaymentEnabled));
            regionService.updateRegion(region);

            LOGGER.debug("Updated region {}", region);
        } catch (final Exception e) {
            throw new RecoverableException("Retrying region change message: " + event, e);
        }
    }

    @Override
    public void notifyRetryLimitReachedError(final String message, final Exception e) {
        final String errorMessage = "Retry limit reached for message: " + message;
        LOGGER.error(errorMessage, e);
    }

}
