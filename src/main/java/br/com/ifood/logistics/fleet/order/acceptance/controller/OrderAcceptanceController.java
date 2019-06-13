package br.com.ccrs.logistics.fleet.order.acceptance.controller;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.OrderDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.http.FleetHttpClientService;
import br.com.ccrs.logistics.fleet.order.acceptance.model.Region;
import br.com.ccrs.logistics.fleet.order.acceptance.service.OrderService;
import br.com.ccrs.logistics.fleet.order.acceptance.service.RegionService;

@RestController
@RequestMapping("/logistics/fleet/order/acceptance")
public class OrderAcceptanceController extends AbstractController {

    @Autowired
    private OrderService orderServiceImpl;

    @Autowired
    private RegionService regionService;

    @Autowired
    private FleetHttpClientService fleetHttpClientService;

    private static final Logger logger = getLogger(OrderAcceptanceController.class);

    @GetMapping
    public Boolean acceptance(@RequestHeader("X-API-KEY") final String apiKey,
                              @RequestHeader("COUNTRY_CODE") final String countryCode,
                              @RequestBody final String rawOrder) {
        try {
            final OrderDTO order = getRawOrderAsDTO(countryCode, rawOrder);
            final Region region = regionService.findByLongitudeLatitude(order.getOrigin().getLongitude(),
                order.getOrigin().getLatitude());

            if ((region != null) && (region.getAcceptanceToggle())) {
                logger.debug("Runned order from orderAcceptance {}", rawOrder);
                return orderServiceImpl.acceptance(order);
            } else {
                logger.debug("Runned order from fleet {}", rawOrder);
                return fleetHttpClientService.acceptance(rawOrder, apiKey);
            }
        } catch (final Exception ex) {
            logger.error("Runned order from fleet due error {} Exception {}", rawOrder, ex);
            return fleetHttpClientService.acceptance(rawOrder, apiKey);
        }
    }
}
