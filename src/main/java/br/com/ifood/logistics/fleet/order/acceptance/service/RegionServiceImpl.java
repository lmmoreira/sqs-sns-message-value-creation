package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.slf4j.LoggerFactory.getLogger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import br.com.ccrs.logistics.fleet.order.acceptance.exception.RegionNotFoundException;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.RegionRepository;

@Service
public class RegionServiceImpl implements RegionService {

    private static final Logger logger = getLogger(RegionServiceImpl.class);

    private final RegionRepository regionRepository;
    private final GisService gisServiceImpl;

    @Autowired
    public RegionServiceImpl(final RegionRepository regionRepository, final GisService gisServiceImpl) {
        this.regionRepository = regionRepository;
        this.gisServiceImpl = gisServiceImpl;
    }

    @Override
    public Optional<Region> findByRegionUuid(final String regionUuid) {
        return regionRepository.findByRegionUuid(regionUuid);
    }

    @Override
    @Transactional
    public Region updateRegion(final Region region) {
        return regionRepository.save(region);
    }

    @Override
    @Transactional
    public void updateSaturatedByRegion(final String regionUuid, final Boolean saturated) {
        final Region region = regionRepository.findByRegionUuid(regionUuid).orElseThrow(RegionNotFoundException::new);
        region.setSaturated(saturated);
        regionRepository.save(region);
    }

    @Override
    @Transactional
    public void normalizeOfflinePaymentByRegion(final String regionUuid) {
        this.updateSaturatedOfflinePaymentByRegion(regionUuid, false);
    }

    @Override
    @Transactional
    public void saturateOfflinePaymentByRegion(final String regionUuid) {
        this.updateSaturatedOfflinePaymentByRegion(regionUuid, true);
    }

    @Override
    public void enableOfflinePaymentByRegion(final String regionUuid) {
        this.updateOfflinePaymentEnabledByRegion(regionUuid, true);
    }

    @Override
    public void disableOfflinePaymentByRegion(final String regionUuid) {
        this.updateOfflinePaymentEnabledByRegion(regionUuid, false);
    }

    @Override
    public Region findByLongitudeLatitude(final Double longitude, final Double latitude) {
        final Map<UUID, Region> regionsMap = regionRepository.findAll()
                .stream()
                .filter(region -> (!StringUtils.isBlank(region.getKml())))
                .collect(Collectors.toMap(Region::getKmlAsUuid,
                        Function.identity()));

        final UUID uuidFromGis = gisServiceImpl.getUuidFromGis(new ArrayList<>(regionsMap.keySet()), longitude, latitude).orElse(null);
        logger.debug("UUID gis sdk result {}", uuidFromGis);
        final Region region = regionsMap.get(uuidFromGis);
        logger.debug("Region result {}", region);

        return region;
    }

    private void updateOfflinePaymentEnabledByRegion(final String regionUuid, final boolean newValue) {
        regionRepository.findByRegionUuid(regionUuid).map(region -> {
            if (region.getOfflinePaymentEnabled().equals(newValue)) {
                return region;
            }
            region.setOfflinePaymentEnabled(newValue);
            return regionRepository.save(region);
        }).orElseThrow(() -> new RegionNotFoundException("Cannot found the Region by uuid: " + regionUuid));
    }

    private void updateSaturatedOfflinePaymentByRegion(final String regionUuid, final boolean newValue) {
        regionRepository.findByRegionUuid(regionUuid).map(region -> {
            if (region.getSaturatedOfflinePayment().equals(newValue)) {
                return region;
            }
            region.setSaturatedOfflinePayment(newValue);
            return regionRepository.save(region);
        }).orElseThrow(() -> new RegionNotFoundException("Cannot found the Region by uuid: " + regionUuid));
    }

}