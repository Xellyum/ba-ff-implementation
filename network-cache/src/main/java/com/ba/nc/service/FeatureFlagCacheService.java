package com.ba.nc.service;

import com.ba.nc.persistence.model.FeatureFlag;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class FeatureFlagCacheService {

    private static final Logger LOGGER = Logger.getLogger(FeatureFlagCacheService.class.getName());

    @Transactional
    public FeatureFlag addFeatureFlagConfig(Map<String, Boolean> featureFlagConfig) {
        var featureFlag = new FeatureFlag();
        featureFlag.setFeatureFlagConfig(featureFlagConfig);
        featureFlag.persist();
        LOGGER.info("Feature flag cached: " + featureFlag);
        return featureFlag;
    }

    public Map<String, Boolean> getLatestFeatureFlagConfig() {
        return Optional.ofNullable(FeatureFlag.findMostRecent())
                .map(FeatureFlag::getFeatureFlagConfig)
                .orElseGet(Collections::emptyMap);
    }
}
