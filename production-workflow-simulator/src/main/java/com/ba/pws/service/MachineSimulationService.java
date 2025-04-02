package com.ba.pws.service;

import com.ba.pws.featureflag.FeatureFlagConfigProvider;
import com.ba.pws.featureflag.model.FeatureFlag;
import com.ba.pws.persistence.model.Item;
import com.ba.pws.persistence.model.ProductionQuality;
import com.ba.pws.persistence.model.ProductionStep;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class MachineSimulationService {

    @Inject
    QualityCheckService qualityCheckService;

    @Inject
    ItemPriorityService itemPriorityService;

    @Inject
    ResourceManagementService resourceManagementService;

    private static final Logger LOGGER = Logger.getLogger(MachineSimulationService.class.getName());

    @Blocking
    @Transactional
    @Scheduled(cron = "{machine.simulation.speed.assembly.cron.expr}")
    public void simulateAssembly() {
        var assemblyItem = itemPriorityService.getNextItemForProductionStep(ProductionStep.ASSEMBLY);
        if (assemblyItem == null) {
            return;
        }

        LOGGER.info("Simulating Assembly for " + assemblyItem);
        increaseProductionStepOfItem(assemblyItem);

        resourceManagementService.consumeResources(ProductionStep.ASSEMBLY, assemblyItem.getType());
    }

    @Blocking
    @Transactional
    @Scheduled(cron = "{machine.simulation.speed.quality.cron.expr}")
    public void simulateQualityCheck() {
        if (!FeatureFlagConfigProvider.isFeatureEnabled(FeatureFlag.QUALITY_CHECK_ENABLED)) {
            return;
        }

        var qualityCheckItem = itemPriorityService.getNextItemForProductionStep(ProductionStep.QUALITY_CHECK);
        if (qualityCheckItem == null) {
            return;
        }

        var productionQuality = qualityCheckService.checkQualityOfItem(qualityCheckItem);
        if (productionQuality.equals(ProductionQuality.FLAWLESS)) {
            LOGGER.info("Simulating Quality Check for " + qualityCheckItem + " -> item was FLAWLESS and can continue to next step");
            increaseProductionStepOfItem(qualityCheckItem);
        }
        else {
            LOGGER.info("Simulating Quality Check for " + qualityCheckItem + " -> item was DAMAGED and requires another assembly");
            qualityCheckItem.setProductionStep(ProductionStep.ASSEMBLY);
        }
    }

    @Blocking
    @Transactional
    @Scheduled(cron = "{machine.simulation.speed.packaging.cron.expr}")
    public void simulatePackaging() {
        var packagingItem = Item.findByProductionStep(ProductionStep.PACKAGING).stream().findFirst().orElse(null);
        if (packagingItem == null) {
            return;
        }

        LOGGER.info("Simulating Packaging for " + packagingItem);
        increaseProductionStepOfItem(packagingItem);
        resourceManagementService.consumeResources(ProductionStep.PACKAGING, packagingItem.getType());
    }

    private void increaseProductionStepOfItem(Item item) {
        item.setProductionStep(item.getProductionStep().nextStep());

        if (!FeatureFlagConfigProvider.isFeatureEnabled(FeatureFlag.QUALITY_CHECK_ENABLED)) {
            if (item.getProductionStep() == ProductionStep.QUALITY_CHECK) {
                increaseProductionStepOfItem(item);
            }
        }
        item.persist();
    }
}
