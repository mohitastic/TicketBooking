package com.booking.toggles;

import org.springframework.stereotype.Component;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.repository.FeatureState;

@Component
public class FeatureToggleUtility {
    public void toggleFeature(Features feature, boolean state, FeatureManager featureManager) {
        FeatureState movieScheduleFeatureState = new FeatureState(feature, state);
        featureManager.setFeatureState(movieScheduleFeatureState);
    }
}
