package com.ba.pws.service;

import com.ba.pws.featureflag.FeatureFlagConfigProvider;
import com.ba.pws.featureflag.model.FeatureFlag;
import com.ba.pws.persistence.model.ItemType;
import com.ba.pws.persistence.model.ProductionStep;
import com.ba.pws.persistence.model.ResourceInventory;
import com.ba.pws.persistence.model.ResourceType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ResourceManagementService {

    @Transactional
    public void consumeResources(ProductionStep productionStep, ItemType type) {
        if (!FeatureFlagConfigProvider.isFeatureEnabled(FeatureFlag.RESOURCE_MANAGEMENT_ENABLED)) {
            return;
        }

        switch (productionStep) {
            case ASSEMBLY -> consumeResourcesForAssembly(type);
            case PACKAGING -> consumeResourcesForPackaging(type);
        }
    }

    public ResourceInventory getCurrentStock(ResourceType type) {
        return ResourceInventory.findById(type);
    }

    private void consumeResourcesForAssembly(ItemType type) {
        switch (type) {
            case BOX -> consume(ResourceType.WOOD, 4);
            case CRATE -> consume(ResourceType.WOOD, 10);
            case PALLET -> consume(ResourceType.WOOD, 2);
        }
    }

    private void consumeResourcesForPackaging(ItemType type) {
        switch (type) {
            case BOX -> consume(ResourceType.PAPER, 2);
            case CRATE -> consume(ResourceType.PAPER, 5);
            case PALLET -> consume(ResourceType.PAPER, 1);
        }
    }

    private void consume(ResourceType type, int amount) {
        var resource = (ResourceInventory) ResourceInventory.findById(type);
        resource.setCurrentStock(resource.getCurrentStock() - amount);
        resource.setRecentlyUsed(resource.getRecentlyUsed() + amount);
        resource.persist();
    }
}
