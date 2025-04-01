package com.ba.pws.service;

import com.ba.pws.featureflag.FeatureFlagConfigProvider;
import com.ba.pws.featureflag.model.FeatureFlag;
import com.ba.pws.persistence.model.Item;
import com.ba.pws.persistence.model.ProductionStep;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Comparator;
import java.util.logging.Logger;

@ApplicationScoped
public class ItemPriorityService {

    private static final Logger LOGGER = Logger.getLogger(ItemPriorityService.class.getName());

    public Item getNextItemForProductionStep(ProductionStep productionStep) {
        var items = Item.findByProductionStep(productionStep);
        if (items.isEmpty()) {
            return null;
        }

        if (FeatureFlagConfigProvider.isFeatureEnabled(FeatureFlag.PRIORITY_QUEUE_ENABLED)) {
            items.sort(Comparator.comparing(Item::getPriority, Comparator.nullsLast(Comparator.reverseOrder())));
        }

        return items.getFirst();
    }
}
