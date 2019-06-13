package br.com.ccrs.logistics.fleet.order.acceptance.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GisService {

    Optional<UUID> getUuidFromGis(List<UUID> uuidList, Double longitude, Double latitude);

}
