package br.com.ccrs.logistics.fleet.order.acceptance.http;


import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers({"Content-Type: application/json"})
public interface FleetHttpClient {

    @RequestLine("GET /acceptance")
    @Headers({"X-API-KEY: {apiKey}", "Cache-Control: no-cache"})
    @Body("{rawOrder}")
    Boolean acceptance(String rawOrder, @Param("apiKey") String apiKey);

    @RequestLine("POST /create")
    @Headers({"X-API-KEY: {apiKey}", "Cache-Control: no-cache"})
    @Body("{rawOrder}")
    String create(String rawOrder, @Param("apiKey") String apiKey);
}
