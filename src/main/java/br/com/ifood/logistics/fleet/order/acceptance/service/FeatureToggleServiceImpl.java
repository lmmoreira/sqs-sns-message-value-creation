package br.com.ccrs.logistics.fleet.order.acceptance.service;

import br.com.ccrs.logistics.fleet.order.acceptance.model.FeatureToggle.FeatureToggleName;
import br.com.ccrs.logistics.fleet.order.acceptance.repository.FeatureToggleRepository;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class FeatureToggleServiceImpl implements FeatureToggleService {

    private static final Logger LOGGER = getLogger(FeatureToggleServiceImpl.class);

    private final FeatureToggleRepository featureToggleRepository;

    public FeatureToggleServiceImpl(final FeatureToggleRepository featureToggleRepository) {
        this.featureToggleRepository = featureToggleRepository;
    }

    @Override
    public boolean isOrderCreateMasterSwitchToggled() {

        return isToggled(FeatureToggleName.ORDER_CREATE_MASTER_SWITCH);
    }

    private boolean isToggled(final FeatureToggleName featureToggleName) {

        final var featureToggle = featureToggleRepository.findById(featureToggleName);
        if (featureToggle.isPresent()) {
            return featureToggle.get().getToggled();
        }
        LOGGER.warn("Cannot find a feature toggle '{}'. Returning toggle value as false.", featureToggleName);
        return false;
    }

}
