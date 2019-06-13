package br.com.ccrs.logistics.fleet.order.acceptance.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
final class FleetHttpClientServiceImpl implements FleetHttpClientService {

    @Autowired
    private FleetHttpClient client;

    @Override
    public Boolean acceptance(final String rawOrder, final String apiKey) {
        return client.acceptance(rawOrder, apiKey);
    }

    @Override
    public String create(final String rawOrder, final String apiKey) {
        return client.create(rawOrder, apiKey);
    }

}
