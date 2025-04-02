package com.ba.pws.kafka;

import com.ba.pws.featureflag.FeatureFlagConfigProvider;
import com.ba.pws.kafka.model.Environment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class KafkaConsumer {

    @Inject
    FeatureFlagConfigProvider featureFlagConfigProvider;

    @ConfigProperty(name = "production.workflow.manager.environment")
    Environment deploymentEnvironment;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumer.class.getName());

    @Incoming("development-flags")
    @Blocking
    public void consumeDevelopmentFlags(String event) {
        if (deploymentEnvironment != Environment.DEVELOPMENT) {
            return;
        }

        LOGGER.info("Consumed Development-Flags event: " + event);
        updateLocalFeatureFlagConfigCache(event);
    }

    @Incoming("stage-flags")
    @Blocking
    public void consumeStageFlags(String event) {
        if (deploymentEnvironment != Environment.STAGE) {
            return;
        }

        LOGGER.info("Consumed Stage-Flags event: " + event);
        updateLocalFeatureFlagConfigCache(event);
    }

    @Incoming("production-flags")
    @Blocking
    public void consumeProductionFlags(String event) {
        if (deploymentEnvironment != Environment.PRODUCTION) {
            return;
        }

        LOGGER.info("Consumed Production-Flags event: " + event);
        updateLocalFeatureFlagConfigCache(event);
    }

    private void updateLocalFeatureFlagConfigCache(String event) {
        // TODO: adjust kafka events to update network cache
        try {
            Map<String, Boolean> features = objectMapper.readValue(event, Map.class);
            featureFlagConfigProvider.updateLocalFeatureFlagConfigCache(features);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
