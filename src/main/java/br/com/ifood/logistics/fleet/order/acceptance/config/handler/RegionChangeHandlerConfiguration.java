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

import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.RegionChangeEvent;

@Configuration
public class RegionChangeHandlerConfiguration extends AbstractHandlerConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public AbstractQueueConsumer regionChangeConsumer(@Qualifier("amazonSQSConfiguration") final AmazonConfiguration amazonConfiguration,
                                                      @Qualifier("regionChangeHandler") final AbstractMessageHandler<String, RegionChangeEvent> messageHandler,
                                                      @Qualifier("regionChangeHandlerWorkerConfiguration") final AmazonSQSQueueWorkerConfiguration handlerConfiguration,
                                                      @Qualifier("amazonSQSSender") final AbstractQueueSender retrySender,
                                                      @Qualifier("amazonSQSRetryOperations") final RetryOperations retryOperations,
                                                      @Value("${handler.region.change.config.dlqqueue}") final String dlqQueue,
                                                      @Value("${handler.region.change.config.retryDelay}") final String retryDelay) {
        return amazonSQSQueueConsumer(amazonConfiguration,
            messageHandler,
            handlerConfiguration,
            retrySender,
            retryOperations,
            dlqQueue,
            retryDelay);
    }

    @Bean(name = "regionChangeHandlerWorkerConfiguration")
    @ConfigurationProperties(prefix = "handler.region.change.config")
    public AmazonSQSQueueWorkerConfiguration regionChangeHandlerWorkerConfiguration() {
        return new AmazonSQSQueueWorkerConfiguration();
    }
}
