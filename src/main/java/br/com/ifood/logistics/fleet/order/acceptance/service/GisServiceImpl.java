package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.slf4j.LoggerFactory.getLogger;
import com.deliverypf.gis.sdk.PolygonSdkService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GisServiceImpl implements GisService {

    private static final Logger logger = getLogger(GisServiceImpl.class);

    private PolygonSdkService polygonSdkService;

    @Autowired
    public GisServiceImpl(final PolygonSdkService polygonSdkService) {
        this.polygonSdkService = polygonSdkService;
    }

    @Override
    @Cacheable(value = "gisCache", key = "{#longitude, #latitude}")
    public Optional<UUID> getUuidFromGis(final List<UUID> uuidList, final Double longitude, final Double latitude) {
        logger.debug("Calling gis sdk api {} {} {}", uuidList, longitude, latitude);
        return polygonSdkService.contains(uuidList, longitude, latitude);
    }

}