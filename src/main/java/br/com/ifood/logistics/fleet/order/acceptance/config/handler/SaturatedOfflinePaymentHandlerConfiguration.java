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

import br.com.ccrs.logistics.fleet.order.acceptance.handler.model.Event;

@Configuration
public class SaturatedOfflinePaymentHandlerConfiguration extends AbstractHandlerConfiguration {


    @Bean(initMethod = "start", destroyMethod = "stop")
    public AbstractQueueConsumer saturatedOfflinePaymentConsumer(@Qualifier("amazonSQSConfiguration") final AmazonConfiguration amazonConfiguration,
                                                                 @Qualifier("saturatedOfflinePaymentHandler") final AbstractMessageHandler<String, Event> messageHandler,
                                                                 @Qualifier("saturatedOfflinePaymentWorkerConfiguration") final AmazonSQSQueueWorkerConfiguration handlerConfiguration,
                                                                 @Qualifier("amazonSQSSender") final AbstractQueueSender retrySender,
                                                                 @Qualifier("amazonSQSRetryOperations") final RetryOperations retryOperations,
                                                                 @Value("${handler.saturated.offline.payment.config.dlqqueue}") final String dlqQueue,
                                                                 @Value("${handler.saturated.offline.payment.config.retryDelay}") final String retryDelay) {
        return amazonSQSQueueConsumer(amazonConfiguration,
            messageHandler,
            handlerConfiguration,
            retrySender,
            retryOperations,
            dlqQueue,
            retryDelay);
    }

    @Bean(name = "saturatedOfflinePaymentWorkerConfiguration")
    @ConfigurationProperties(prefix = "handler.saturated.offline.payment.config")
    public AmazonSQSQueueWorkerConfiguration saturatedOfflinePaymentWorkerConfiguration() {
        return new AmazonSQSQueueWorkerConfiguration();
    }

}
