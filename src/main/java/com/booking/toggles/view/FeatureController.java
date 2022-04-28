package com.booking.toggles.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/features")
public class FeatureController {

    @Autowired
    FeatureManager featureManager;

    @GetMapping()
    public Map<String, Boolean> getFeatureToggles(){
        HashMap<String, Boolean> featureToggles = new HashMap<>();

        for (Feature feature : featureManager.getFeatures()) {
            featureToggles.put(feature.name(), featureManager.isActive(feature));
        }

        return featureToggles;
    }
}
