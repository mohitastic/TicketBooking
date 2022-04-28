package com.booking.toggles;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum Features implements Feature {

    @Label("Movie schedule Feature")
    MOVIE_SCHEDULE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}

