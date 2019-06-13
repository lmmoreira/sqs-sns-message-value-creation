package br.com.ccrs.logistics.fleet.order.acceptance.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfiguration {

    @SuppressWarnings("unchecked")
    @Bean
    public Jackson2ObjectMapperBuilder configureObjectMapper() {
        return new Jackson2ObjectMapperBuilder().modulesToInstall(Hibernate5Module.class)
                .featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

}