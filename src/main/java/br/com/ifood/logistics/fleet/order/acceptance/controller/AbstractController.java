package br.com.ccrs.logistics.fleet.order.acceptance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import br.com.ccrs.logistics.fleet.order.acceptance.controller.dto.OrderDTO;
import br.com.ccrs.logistics.fleet.order.acceptance.exception.OrderDTOMappingException;

public abstract class AbstractController {

    @Autowired
    protected ObjectMapper jacksonObjectMapper;

    protected OrderDTO getRawOrderAsDTO(final String countryCode, final String rawOrder) {
        try {
            final OrderDTO orderDTO = jacksonObjectMapper.readValue(rawOrder, OrderDTO.class);
            orderDTO.setTenantIdentifier(countryCode.toLowerCase());
            return orderDTO;
        } catch (final IOException e) {
            throw new OrderDTOMappingException(e);
        }
    }

}
