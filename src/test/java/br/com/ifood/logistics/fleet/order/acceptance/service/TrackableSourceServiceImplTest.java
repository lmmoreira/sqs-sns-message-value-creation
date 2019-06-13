package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import br.com.ccrs.logistics.fleet.order.acceptance.model.TrackableSource;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.TrackableSourceRepository;

public class TrackableSourceServiceImplTest {

    private TrackableSourceService trackableSourceServiceImpl;
    private TrackableSourceRepository trackableSourceRepository;
    private TrackableSource trackableSource;

    @Before
    public void before() {
        trackableSource = new TrackableSource();

        trackableSourceRepository = mock(TrackableSourceRepository.class);
        when(trackableSourceRepository.findByName(any())).thenReturn(Optional.of(trackableSource));

        trackableSourceServiceImpl = new TrackableSourceServiceImpl(trackableSourceRepository);
    }

    @Test
    public void findByNameTest(){
        Assert.assertEquals(trackableSourceServiceImpl.findByName(any()), trackableSource);
    }

}
