package br.com.ccrs.logistics.fleet.order.acceptance.config.handler;

import com.ccrs.event.AbstractMessageHandler;
import com.ccrs.event.AbstractQueueConsumer;
import com.ccrs.event.AbstractQueueSender;
import com.ccrs.event.amazon.config.AmazonConfiguration;
import com.ccrs.event.amazon.sqs.config.AmazonSQSQueueWorkerConfiguration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryOperations;

import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.Event;

@Configuration
public class OrderStateGiveUpHandlerConfiguration extends AbstractHandlerConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public AbstractQueueConsumer orderGiveUpConsumer(@Qualifier("amazonSQSConfiguration") final AmazonConfiguration amazonConfiguration,
                                                         @Qualifier("orderGiveUpHandler") final AbstractMessageHandler<String, Event> messageHandler,
                                                         @Qualifier("orderGiveUpHandlerConfig") final AmazonSQSQueueWorkerConfiguration handlerConfiguration,
                                                         @Qualifier("amazonSQSSender") final AbstractQueueSender retrySender,
                                                         @Qualifier("amazonSQSRetryOperations") final RetryOperations retryOperations,
                                                         @Value("${handler.order.state.change.config.retryDelay}") final String retryDelay) {
        return amazonSQSQueueConsumer(amazonConfiguration,
            messageHandler,
            handlerConfiguration,
            retrySender,
            retryOperations,
            null,
            retryDelay);
    }

    @Bean(name = "orderGiveUpHandlerConfig")
    @ConfigurationProperties(prefix = "handler.order.state.change.config")
    public AmazonSQSQueueWorkerConfiguration workerOrderGiveUpHandlerConfiguration() {
        return new AmazonSQSQueueWorkerConfiguration();
    }
}
