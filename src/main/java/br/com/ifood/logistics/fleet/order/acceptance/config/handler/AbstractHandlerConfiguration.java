package br.com.ccrs.logistics.fleet.order.acceptance.config.handler;

import java.util.Objects;

import org.springframework.retry.RetryOperations;

import com.ccrs.event.AbstractMessageHandler;
import com.ccrs.event.AbstractQueueConsumer;
import com.ccrs.event.AbstractQueueSender;
import com.ccrs.event.amazon.config.AmazonConfiguration;
import com.ccrs.event.amazon.sqs.AmazonSQSQueueConsumerBuilder;
import com.ccrs.event.amazon.sqs.config.AmazonSQSQueueWorkerConfiguration;
import com.ccrs.event.config.QueueWorkerRetryConfiguration;

public abstract class AbstractHandlerConfiguration {

    protected <T> AbstractQueueConsumer amazonSQSQueueConsumer(final AmazonConfiguration amazonConfiguration,
                                                               final AbstractMessageHandler<String, T> messageHandler,
                                                               final AmazonSQSQueueWorkerConfiguration handlerConfiguration,
                                                               final AbstractQueueSender retrySender,
                                                               final RetryOperations retryOperations,
                                                               final String dlqQueueName, final String retryDelay) {
        final QueueWorkerRetryConfiguration retryConfiguration =
            createQueueWorkerRetryConfiguration(retrySender, dlqQueueName, retryDelay);
        return AmazonSQSQueueConsumerBuilder.get()
                .withAmazonConfiguration(amazonConfiguration)
                .withAmazonSQSWorkerConfiguration(handlerConfiguration)
                .withQueueWorkerRetryConfiguration(retryConfiguration)
                .withMessageHandler(messageHandler)
                .withRetryOperations(retryOperations)
                .build();
    }

    protected QueueWorkerRetryConfiguration createQueueWorkerRetryConfiguration(final AbstractQueueSender retrySender,
                                                                                final String dlqQueueName,
                                                                                final String retryDelay) {
        final QueueWorkerRetryConfiguration retryConfiguration = new QueueWorkerRetryConfiguration();
        retryConfiguration.setRetrySender(retrySender);
        retryConfiguration.setDlqQueue(dlqQueueName);
        if (Objects.nonNull(retryDelay) && !retryDelay.isBlank()) {
            retryConfiguration.setRetryDelay(Integer.valueOf(retryDelay));
        }
        return retryConfiguration;
    }
}
