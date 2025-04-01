package com.ba.pws.featureflag;

import com.ba.pws.featureflag.model.FeatureFlag;
import com.ba.pws.kafka.model.Environment;
import com.ba.pws.rest.client.FeatureFlagClient;
import com.ba.pws.rest.client.NetworkCacheClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.File;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

@ApplicationScoped
public class FeatureFlagConfigProvider {

    @RestClient
    FeatureFlagClient featureFlagClient;

    @RestClient
    NetworkCacheClient networkCacheClient;

    @ConfigProperty(name = "production.workflow.manager.environment")
    Environment deploymentEnvironment;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static Map<String, Boolean> featureFlagConfig = new HashMap<>();

    private static final String FEATURE_FLAG_CONFIG_PATH = "./src/main/resources/feature_flags.json";

    private static final Logger LOGGER = Logger.getLogger(FeatureFlagConfigProvider.class.getName());

    public static boolean isFeatureEnabled(FeatureFlag featureFlag) {
        var value = featureFlagConfig.get(featureFlag.getValue());
        return Objects.requireNonNullElse(value, false);
    }

    @Startup
    @Retry(maxRetries = 2, delay = 3000)
    @Timeout(value = 2000, unit = ChronoUnit.MILLIS)
    @Fallback(fallbackMethod = "loadFeatureFlagConfigFromNetworkCache")
    public void loadInitialFeatureFlagConfig() {
        LOGGER.info("Trying to load feature flag config from FFMS...");
        featureFlagConfig = featureFlagClient.getFeatureFlags(deploymentEnvironment.name());
        LOGGER.info("Successfully got feature flag config (" + deploymentEnvironment + ") from FFMS: " + featureFlagConfig.toString());
    }

    public void updateLocalFeatureFlagConfigCache(Map<String, Boolean> featureFlags) {
        try {
            featureFlagConfig = featureFlags;
            //objectMapper.writeValue(new File(FEATURE_FLAG_CONFIG_PATH), featureFlags);
            LOGGER.info("Updated local feature flag config (" + deploymentEnvironment + "): " + featureFlagConfig.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFeatureFlagConfigFromLocalCache() {
        try {
            LOGGER.warning("Could not get feature flag config (" + deploymentEnvironment + ") from FFMS, using local cache instead");

            featureFlagConfig = objectMapper.readValue(new File(FEATURE_FLAG_CONFIG_PATH), Map.class);
            LOGGER.info("Successfully got feature flag config (" + deploymentEnvironment + ") from local cache: " + featureFlagConfig.toString());
        } catch (IOException e) {
            LOGGER.severe("Could not get feature flag config from local cache, service will not be able to function");
        }
    }

    private void loadFeatureFlagConfigFromNetworkCache() {
        try {
            LOGGER.warning("Could not get feature flag config (" + deploymentEnvironment + ") from FFMS, trying network cache instead");
            featureFlagConfig = networkCacheClient.getFeatureFlagConfig();
            LOGGER.info("Successfully got feature flag config (" + deploymentEnvironment + ") from network cache: " + featureFlagConfig.toString());
        } catch (WebApplicationException e) {
            LOGGER.severe("Could not get feature flag config from network cache, service will not be able to function properly");
        }
    }
}
