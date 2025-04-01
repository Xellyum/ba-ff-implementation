package com.ba.ffms.service;

import com.ba.ffms.persistence.model.Environment;
import com.ba.ffms.persistence.model.FeatureFlag;
import com.ba.ffms.rest.model.FeatureFlagEnvDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class FlagEvaluationService {

    public Map<String, Boolean> evaluateFlags(String feature, Environment environment) {
        return FeatureFlag.findByFeature(feature).stream().collect(
                Collectors.toMap(ff -> ff.feature, ff -> ff.getEnvironments().contains(environment) == ff.active));
    }

    public Map<String, Boolean> evaluateFlags(Environment environment) {
        var featureFlags = FeatureFlag.findByEnvironment(environment);
        return featureFlags.stream().collect(Collectors.toMap(ff -> ff.feature, ff -> ff.active));
    }

    public FeatureFlagEnvDTO evaluateFlags(String feature) {
        var featureFlags = FeatureFlag.findByFeature(feature);

        var flagsByEnv = new EnumMap<Environment, Map<String, Boolean>>(Environment.class);

        for (var env : Environment.values()) {
            flagsByEnv.put(env, featureFlags.stream().collect(Collectors.toMap(
                            ff -> ff.feature,
                            ff -> ff.getEnvironments().contains(env) == ff.active)));
        }

        return new FeatureFlagEnvDTO(
                flagsByEnv.get(Environment.DEVELOPMENT),
                flagsByEnv.get(Environment.STAGE),
                flagsByEnv.get(Environment.PRODUCTION)
        );
    }
}
