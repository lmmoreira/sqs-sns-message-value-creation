package br.com.ccrs.logistics.fleet.order.acceptance.http;

public interface FleetHttpClientService {

    Boolean acceptance(String rawOrder, String apiKey);

    String create(String rawOrder, String apiKey);

}
