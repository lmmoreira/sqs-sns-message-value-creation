package br.com.ccrs.logistics.fleet.order.acceptance;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ccrs.event.AbstractQueueConsumer;
import com.ccrs.event.AbstractQueueSender;
import com.ccrs.event.AbstractTopicSender;
import com.ccrs.event.amazon.config.AmazonConfiguration;
import com.ccrs.event.amazon.sqs.AmazonSQSPrePublishingMessageProcessor;
import com.ccrs.event.amazon.sqs.config.AmazonSQSQueueWorkerConfiguration;

@TestConfiguration
@EnableAutoConfiguration
public class TestConfig {

    @MockBean
    private AmazonConfiguration amazonSQSConfiguration;

    @MockBean
    private AbstractTopicSender topicSender;

    @MockBean
    private AbstractQueueSender amazonSQSSender;

    @MockBean(name = "regionChangeConsumer")
    private AbstractQueueConsumer regionChangeConsumer;

    @MockBean(name = "saturatedRegionHandlerConfig")
    private AmazonSQSQueueWorkerConfiguration saturatedRegionHandlerConfig;

    @MockBean(name = "saturatedOfflinePaymentWorkerConfiguration")
    private AmazonSQSQueueWorkerConfiguration saturatedOfflinePaymentWorkerConfiguration;

    @MockBean(name = "regionChangeHandlerWorkerConfiguration")
    private AmazonSQSQueueWorkerConfiguration regionChangeHandlerWorkerConfiguration;

    @MockBean(name = "saturatedOfflinePaymentConsumer")
    private AbstractQueueConsumer saturatedOfflinePaymentConsumer;

    @MockBean(name = "saturatedRegionConsumer")
    private AbstractQueueConsumer saturatedRegionConsumer;

    @MockBean(name = "orderCreatedConsumer")
    private AbstractQueueConsumer orderCreatedConsumer;

    @MockBean(name = "orderGiveUpConsumer")
    private AbstractQueueConsumer orderGiveUpConsumer;

    @MockBean(name = "amazonSQSPrePublishingMessageProcessor")
    private AmazonSQSPrePublishingMessageProcessor amazonSQSPrePublishingMessageProcessor;

}
