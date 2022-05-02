package com.booking.toggles;

import org.togglz.core.Feature;
import org.togglz.core.annotation.Label;
import org.togglz.core.context.FeatureContext;

public enum Features implements Feature {

    @Label("Movie schedule Feature")
    MOVIE_SCHEDULE,

    @Label("Customer sign up Feature")
    CUSTOMER_SIGN_UP,

    @Label("Change password Feature")
    CHANGE_PASSWORD,

    @Label("View user profile Feature")
    VIEW_USER_PROFILE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }

}

