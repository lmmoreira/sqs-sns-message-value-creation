package br.com.ccrs.logistics.fleet.order.acceptance.controller;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.OrderDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.http.FleetHttpClientService;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;
import br.com.ccrs.logistics.fleet.order.acceptance.service.FeatureToggleService;
import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderService;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;

@RestController
@RequestMapping("/logistics/fleet/order/create")
public class OrderCreateController extends AbstractController {

    private static final Logger LOGGER = getLogger(OrderCreateController.class);

    private final OrderService orderService;
    private final FleetHttpClientService httpClientService;
    private final RegionService regionService;
    private final FeatureToggleService featureToggleService;

    public OrderCreateController(final OrderService orderService, final FleetHttpClientService httpClientService,
                                 final RegionService regionService, final FeatureToggleService featureToggleService) {

        this.orderService = orderService;
        this.httpClientService = httpClientService;
        this.regionService = regionService;
        this.featureToggleService = featureToggleService;
    }

    @PostMapping
    public String create(@RequestHeader("X-API-KEY") final String apiKey,
                         @RequestHeader("COUNTRY_CODE") final String countryCode, //
                         @RequestBody final String rawOrder) {

        LOGGER.debug("New order create request, X-API-KEY: {}, country code: {}", apiKey, countryCode);
        final OrderDTO order = getRawOrderAsDTO(countryCode, rawOrder);
        if (isMasterSwitchToggled() && isRegionToggled(order)) {
            LOGGER.debug("Run order create in house");
            try {
                return orderService.createOrder(countryCode, rawOrder, apiKey, order);
            } catch (final Exception e) {
                LOGGER.warn("Cannot create an Order, calling fleet-api directly", e);
            }
        }
        LOGGER.debug("Run order create directly on fleet-api");
        return httpClientService.create(rawOrder, apiKey);
    }

    private boolean isMasterSwitchToggled() {
        return featureToggleService.isOrderCreateMasterSwitchToggled();
    }

    private boolean isRegionToggled(final OrderDTO order) {

        final Double longitude = order.getOrigin().getLongitude();
        final Double latitude = order.getOrigin().getLatitude();
        final Region region = regionService.findByLongitudeLatitude(longitude, latitude);
        return Objects.nonNull(region) && region.getCreateToggle();
    }

}
