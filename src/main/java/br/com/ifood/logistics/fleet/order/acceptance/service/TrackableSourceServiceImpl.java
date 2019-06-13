package br.com.ccrs.logistics.fleet.order.acceptance.service;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ccrs.logistics.fleet.order.acceptance.exception.TrackableSourceNotFoundException;
import br.com.ccrs.logistics.fleet.order.acceptance.model.TrackableSource;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.TrackableSourceRepository;

@Service
public class TrackableSourceServiceImpl implements TrackableSourceService {

    private static final Logger logger = getLogger(TrackableSourceServiceImpl.class);

    private final TrackableSourceRepository trackableSourceRepository;

    @Autowired
    public TrackableSourceServiceImpl(final TrackableSourceRepository trackableSourceRepository) {
        this.trackableSourceRepository = trackableSourceRepository;
    }

    @Override
    public TrackableSource findByName(final String name) {
        return trackableSourceRepository.findByName(name).orElseThrow(TrackableSourceNotFoundException::new);
    }

}