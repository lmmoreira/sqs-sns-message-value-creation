package br.com.ccrs.logistics.fleet.order.acceptance.http;

import com.google.common.io.CharStreams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;

import br.com.ccrs.logistics.fleet.order.acceptance.exception.FleetHttpClientException;
import feign.Response;
import feign.codec.ErrorDecoder;

@Component("fleetErrorDecoder")
final class FleetErrorDecoder implements ErrorDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(FleetErrorDecoder.class);

    @Override
    public Exception decode(final String methodKey, final Response response) {
        try (InputStreamReader inputStreamReader = new InputStreamReader(response.body().asInputStream())) {
            LOGGER.warn("Fleet returned {} with body {}", response.status(), CharStreams.toString(inputStreamReader));
        } catch (final IOException e) {
            LOGGER.warn("Could not parse response body to a String ", response);
            LOGGER.error("Exception: ", e);
        }

        return new FleetHttpClientException("Error on parsing fleet");
    }

}
