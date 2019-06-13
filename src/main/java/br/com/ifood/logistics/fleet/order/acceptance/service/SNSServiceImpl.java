package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.slf4j.LoggerFactory.getLogger;

import com.ccrs.event.AbstractTopicSender;
import com.ccrs.event.exceptions.InvalidMessageException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

import br.com.ccrs.logistics.fleet.order.acceptance.exception.SNSPublishingException;

@Service
public class SNSServiceImpl implements SNSService {

    private static final Logger logger = getLogger(SNSServiceImpl.class);

    private AbstractTopicSender topicSender;

    @Autowired
    public SNSServiceImpl(final @Qualifier("topicSender") AbstractTopicSender topicSender) {
        this.topicSender = topicSender;
    }

    @Override
    public void publish(final String topic, final String message, final Map<String, String> attributes) {

        try {
            logger.debug("Sending to topic {} event {} attributes", topic, message, attributes);
            topicSender.sendMessage(topic, message, true, attributes);
        } catch (final InvalidMessageException e) {
            throw new SNSPublishingException("Event couldn't be published to topic " + topic + ". Event: " + message + ". Attributes" + attributes, e);
        }
    }
}