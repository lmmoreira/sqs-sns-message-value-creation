package br.com.ccrs.logistics.fleet.order.acceptance.service;

import br.com.ccrs.logistics.fleet.order.acceptance.model.TrackableSource;

public interface TrackableSourceService {
    TrackableSource findByName(final String name);
}
