package br.com.ccrs.logistics.fleet.order.acceptance.config.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryOperations;

import com.ccrs.event.AbstractMessageHandler;
import com.ccrs.event.AbstractQueueConsumer;
import com.ccrs.event.AbstractQueueSender;
import com.ccrs.event.amazon.config.AmazonConfiguration;
import com.ccrs.event.amazon.sqs.config.AmazonSQSQueueWorkerConfiguration;

import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.OrderCreatedEvent;

@Configuration
public class OrderCreatedHandlerConfiguration extends AbstractHandlerConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public AbstractQueueConsumer orderCreatedConsumer(@Qualifier("amazonSQSConfiguration") final AmazonConfiguration amazonConfiguration,
                                                      @Qualifier("orderCreatedHandler") final AbstractMessageHandler<String, OrderCreatedEvent> messageHandler,
                                                      @Qualifier("orderCreatedHandlerWorkerConfiguration") final AmazonSQSQueueWorkerConfiguration handlerConfiguration,
                                                      @Qualifier("amazonSQSSender") final AbstractQueueSender retrySender,
                                                      @Qualifier("amazonSQSRetryOperations") final RetryOperations retryOperations,
                                                      @Value("${handler.order.created.config.retryDelay}") final String retryDelay) {
        return amazonSQSQueueConsumer(amazonConfiguration,
            messageHandler,
            handlerConfiguration,
            retrySender,
            retryOperations,
            null,
            retryDelay);
    }

    @Bean(name = "orderCreatedHandlerWorkerConfiguration")
    @ConfigurationProperties(prefix = "handler.order.created.config")
    public AmazonSQSQueueWorkerConfiguration orderCreatedHandlerWorkerConfiguration() {
        return new AmazonSQSQueueWorkerConfiguration();
    }
}
