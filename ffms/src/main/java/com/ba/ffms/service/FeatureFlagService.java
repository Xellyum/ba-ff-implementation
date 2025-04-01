package com.ba.ffms.service;

import com.ba.ffms.kafka.KafkaProducer;
import com.ba.ffms.persistence.model.Environment;
import com.ba.ffms.persistence.model.FeatureFlag;
import com.ba.ffms.rest.model.FeatureFlagDTO;
import com.ba.ffms.rest.model.FeatureFlagEnvDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class FeatureFlagService {

    @Inject
    FlagEvaluationService flagEvaluationService;

    @Inject
    KafkaProducer kafkaProducer;

    private static final Logger LOGGER = Logger.getLogger(FeatureFlagService.class.getName());

    @Transactional
    public FeatureFlag deleteFeatureFlag(String featureFlagId) {
        var featureFlag = (FeatureFlag) FeatureFlag.findById(featureFlagId);
        if (featureFlag == null) {
            LOGGER.info("Could not delete Feature Flag because it does not exist: " + featureFlagId);
            return null;
        }

        featureFlag.delete();
        LOGGER.info("Deleted Feature Flag: " + featureFlagId);

        sendFeatureFlagEvents();

        return featureFlag;
    }

    @Transactional
    public FeatureFlag createFeatureFlag(FeatureFlagDTO featureFlagDTO) {
        if (FeatureFlag.findById(featureFlagDTO.feature()) != null) {
            LOGGER.info("New feature Flag could not be created because it already exists: " + featureFlagDTO.feature());
            return null;
        }

        var featureFlag = new FeatureFlag();
        featureFlag.setFeature(featureFlagDTO.feature());
        featureFlag.setActive(featureFlagDTO.active());
        featureFlag.setEnvironments(featureFlagDTO.environments());
        featureFlag.setDescription(featureFlagDTO.description());
        featureFlag.setDependencies(featureFlagDTO.dependencies());
        featureFlag.createdAt = Instant.now();
        featureFlag.persist();
        LOGGER.info("Feature flag created: " + featureFlag);

        sendFeatureFlagEvents();

        return featureFlag;
    }

    @Transactional
    public FeatureFlag editFeatureFlag(FeatureFlagDTO featureFlagDTO) {
        var featureFlag = (FeatureFlag) FeatureFlag.findById(featureFlagDTO.feature());
        if (featureFlag == null) {
            LOGGER.info("Could not edit Feature Flag because it does not exist: " + featureFlagDTO.feature());
            return null;
        }

        featureFlag.setActive(featureFlagDTO.active());
        featureFlag.setEnvironments(featureFlagDTO.environments());
        featureFlag.setDescription(featureFlagDTO.description());
        featureFlag.setDependencies(featureFlagDTO.dependencies());
        featureFlag.persist();

        LOGGER.info("Edited Feature Flag: " + featureFlag);

        sendFeatureFlagEvents();

        return featureFlag;
    }

    public Map<String, Boolean> getFeatureFlag(String feature, Environment environment) {
        return flagEvaluationService.evaluateFlags(feature, environment);
    }

    public Map<String, Boolean> getFeatureFlagsByEnvironment(Environment environment) {
        return flagEvaluationService.evaluateFlags(environment);
    }

    public FeatureFlagEnvDTO getFeatureFlagsByFeature(String feature) {
        return flagEvaluationService.evaluateFlags(feature);
    }

    public List<FeatureFlag> getAllFeatureFlags() {
        return FeatureFlag.findAll().list();
    }

    public void sendFeatureFlagEvents() {
        kafkaProducer.sendDevelopmentFlagEvent(getFeatureFlagsByEnvironment(Environment.DEVELOPMENT));
        kafkaProducer.sendStageFlagEvent(getFeatureFlagsByEnvironment(Environment.STAGE));
        kafkaProducer.sendProductionFlagEvent(getFeatureFlagsByEnvironment(Environment.PRODUCTION));
    }
}
