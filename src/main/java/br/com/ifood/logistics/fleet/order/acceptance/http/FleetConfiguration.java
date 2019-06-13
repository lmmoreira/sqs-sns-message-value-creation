package br.com.ccrs.logistics.fleet.order.acceptance.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ccrs.api.http.client.ApacheHttpClients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import feign.Feign;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.httpclient.ApacheHttpClient;

@Configuration
@ComponentScan(basePackages = "com.deliverypf.gis.geo.sdk.service")
public class FleetConfiguration {

        @Value("${fleet.http.client.url}")
        private String url;

        @Autowired
        protected ObjectMapper jacksonObjectMapper;

        @Bean
        @ConditionalOnMissingBean(FleetHttpClient.class)
        public FleetHttpClient fleetHttpClient(@Qualifier(value = "fleetErrorDecoder") final ErrorDecoder errorDecoder) {
            return Feign.builder()
                    .decoder(new GsonDecoder())
                    .client(new ApacheHttpClient(ApacheHttpClients.builder().build()))
                    .target(FleetHttpClient.class, url);
        }

}
