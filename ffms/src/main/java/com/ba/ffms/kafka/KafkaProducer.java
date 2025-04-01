package com.ba.ffms.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class KafkaProducer {

    @Channel("development-flags-out")
    Emitter<Map<String, Boolean>> developmentFlagEmitter;

    @Channel("stage-flags-out")
    Emitter<Map<String, Boolean>> stageFlagEmitter;

    @Channel("production-flags-out")
    Emitter<Map<String, Boolean>> productionFlagEmitter;

    private static final Logger LOGGER = Logger.getLogger(KafkaProducer.class.getName());

    public void sendDevelopmentFlagEvent(Map<String, Boolean> event) {
        developmentFlagEmitter.send(event);
        LOGGER.info("Sent Development Flag Event: " + event);
    }

    public void sendStageFlagEvent(Map<String, Boolean> event) {
        stageFlagEmitter.send(event);
        LOGGER.info("Sent Stage Flag Event: " + event);
    }

    public void sendProductionFlagEvent(Map<String, Boolean> event) {
        productionFlagEmitter.send(event);
        LOGGER.info("Sent Production Flag Event: " + event);
    }
}
