package br.com.ccrs.logistics.fleet.order.acceptance.controller;

import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Locale;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ccrs.logistics.fleet.order.acceptance.config.JacksonConfiguration;
import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.LocalityDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.OrderDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.model.PaymentType;

public class AbstractControllerTest {

    private final ObjectMapper objectMapper = new JacksonConfiguration().configureObjectMapper().build();

    @Test
    public void shouldSetCountryCodeAsTenantIdentifier() {
        final AbstractController controller = new AbstractController() {
        };
        controller.jacksonObjectMapper = objectMapper;

        final String countryCode = "BR";
        final OrderDTO dto = controller.getRawOrderAsDTO(countryCode, createOrderPayload());

        assertEquals(countryCode.toLowerCase(), dto.getTenantIdentifier());
    }

    private String createOrderPayload() {
        final LocalityDTO origin = createLocality();
        final LocalityDTO destination = createLocality();
        final OrderDTO orderDTO = new OrderDTO("trackableSourceName", "externalId", ZonedDateTime.now(), origin, destination,
            PaymentType.CARD, 1L, 1L, "orderType", Currency.getInstance(Locale.US));

        return convertToJson(orderDTO);
    }

    private LocalityDTO createLocality() {
        return new LocalityDTO("name", "externalId", "street", "district", "city", "state", "SP",
            "country", "MX", 1D, 1D, ZonedDateTime.now());
    }

    private String convertToJson(final OrderDTO orderDTO) {
        try {
            return objectMapper.writeValueAsString(orderDTO);
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot convert to json, object invalid.");
        }
    }

}
