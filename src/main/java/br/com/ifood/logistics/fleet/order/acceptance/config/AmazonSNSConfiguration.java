package br.com.ccrs.logistics.fleet.order.acceptance.config;

import com.ccrs.event.AbstractTopicSender;
import com.ccrs.event.amazon.config.AmazonConfiguration;
import com.ccrs.event.amazon.sns.AmazonSNSTopicSender;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class AmazonSNSConfiguration {

    @Bean("fleetAmazonSnsConfiguration")
    public AmazonConfiguration snsLocalConfiguration(@Qualifier("amazonSQSConfiguration") final AmazonConfiguration configuration,
                                                     @Value("${sns.amazon.local.endpoint:}") final String localEndpoint) {
        final AmazonConfiguration result = new AmazonConfiguration();
        result.setAccessKeyId(configuration.getAccessKeyId());
        result.setSecretKey(configuration.getSecretKey());
        result.setRegion(configuration.getRegion());
        result.setUseIAMRoles(configuration.isUseIAMRoles());
        result.setAccount(configuration.getAccount());
        result.setEndpoint(configuration.getEndpoint());
        if (StringUtils.isNotBlank(localEndpoint)) {
            result.setEndpoint(localEndpoint);
        }
        return result;
    }

    @Bean("topicSender")
    @Autowired
    public AbstractTopicSender eventTopicSender(@Qualifier("fleetAmazonSnsConfiguration") final AmazonConfiguration configuration,
                                                @Qualifier("snsSenderRetryOperations") final RetryOperations retryOperations) {
        return AmazonSNSTopicSender.of(configuration, retryOperations);
    }

    @Bean(name = "snsSenderRetryOperations")
    public RetryOperations sqsSenderRetryOperations() {
        return new RetryTemplate();
    }

}
