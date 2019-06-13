package br.com.ccrs.logistics.fleet.order.acceptance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"br.com.ccrs.logistics.fleet.order.acceptance", "com.deliverypf.gis",
    "com.ccrs.api.filters"})
public class FleetOrderAcceptanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FleetOrderAcceptanceApplication.class, args);
    }
}
