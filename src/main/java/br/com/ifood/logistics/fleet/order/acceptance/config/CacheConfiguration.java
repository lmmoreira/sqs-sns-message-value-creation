package br.com.ccrs.logistics.fleet.order.acceptance.config;

import com.github.benmanes.caffeine.cache.Caffeine;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CaffeineCache gisCache() {
        return new CaffeineCache("gisCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(4, TimeUnit.HOURS)
                        .build());
    }

}