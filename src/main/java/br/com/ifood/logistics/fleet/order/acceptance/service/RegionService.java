package br.com.ccrs.logistics.fleet.order.acceptance.service;

import java.util.Optional;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;

public interface RegionService {

    void normalizeOfflinePaymentByRegion(String regionUuid);

    void saturateOfflinePaymentByRegion(String regionUuid);

    void updateSaturatedByRegion(String regionUuid, Boolean saturated);

    Region findByLongitudeLatitude(Double longitude, Double latitude);

    void enableOfflinePaymentByRegion(String regionUuid);

    void disableOfflinePaymentByRegion(String regionUuid);

    Optional<Region> findByRegionUuid(final String regionUuid);

    Region updateRegion(Region region);

}
