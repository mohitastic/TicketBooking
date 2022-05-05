package com.booking.toggles;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum Features implements Feature {

    @Label("Movie schedule Feature")
    MOVIE_SCHEDULE,

    @Label("Change password Feature")
    CHANGE_PASSWORD,

    @Label("View user profile Feature")
    VIEW_USER_PROFILE,

    @Label("Customer booking")
    CUSTOMER_BOOKING;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}

