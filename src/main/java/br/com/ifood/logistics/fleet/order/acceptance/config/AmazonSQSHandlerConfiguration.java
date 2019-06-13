package br.com.ccrs.logistics.fleet.order.acceptance.config;

import static org.slf4j.LoggerFactory.getLogger;

import com.ccrs.event.AbstractQueueSender;
import com.ccrs.event.amazon.config.AmazonConfiguration;
import com.ccrs.event.amazon.sqs.AmazonSQSPrePublishingMessageProcessor;
import com.ccrs.event.amazon.sqs.AmazonSQSQueueSenderBuilder;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class AmazonSQSHandlerConfiguration {

    private static final Logger logger = getLogger(AmazonSQSHandlerConfiguration.class);

    @Bean("amazonSQSConfiguration")
    @ConfigurationProperties(prefix = "amazon")
    @Primary
    public AmazonConfiguration amazonSQSConfiguration() {
        return new AmazonConfiguration();
    }

    @Bean(name = "amazonSQSRetryOperations")
    public RetryOperations sqsConsumerRetryOperations() {
        return new RetryTemplate();
    }

    @Bean("amazonSQSSender")
    public AbstractQueueSender amazonSQSSender(@Qualifier("amazonSQSConfiguration") final AmazonConfiguration amazonConfiguration,
                                               @Qualifier("amazonSQSRetryOperations") final RetryOperations retryOperations) {

        return AmazonSQSQueueSenderBuilder.get()
                .configuration(amazonConfiguration)
                .retryOperations(retryOperations)
                .prePublishingMessageProcessor(AmazonSQSPrePublishingMessageProcessor.nopProcessor.INSTANCE)
                .build();
    }

}
