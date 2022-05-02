package com.booking.exceptions;

public class FeatureDisabledException extends RuntimeException {
    public FeatureDisabledException() {
        super("Feature Disabled");
    }
}
