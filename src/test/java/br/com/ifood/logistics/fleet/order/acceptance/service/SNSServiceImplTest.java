package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ccrs.event.AbstractTopicSender;
import com.ccrs.event.exceptions.InvalidMessageException;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class SNSServiceImplTest {

    private SNSService snsServiceImpl;
    private AbstractTopicSender topicSender;


    @Before
    public void before() {
        topicSender = mock(AbstractTopicSender.class);
        snsServiceImpl = new SNSServiceImpl(topicSender);
    }

    @Test
    public void publishTest() throws InvalidMessageException {
        snsServiceImpl.publish("", "", new HashMap<>());
        verify(topicSender, times(1)).sendMessage(any(), any(), any(), any());
    }

}
