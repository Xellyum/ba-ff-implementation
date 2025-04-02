package com.ba.pws.service;

import com.ba.pws.persistence.model.Item;
import com.ba.pws.persistence.model.ProductionQuality;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@ApplicationScoped
public class QualityCheckService {

    private final Random random = new Random();

    private static final double QUALITY_CHECK_DAMAGED_POSSIBILITY = 0.4;

    private static final Logger LOGGER = Logger.getLogger(QualityCheckService.class.getName());

    public ProductionQuality checkQualityOfItem(Item item) {
        if (itemIsDamaged(item)) {
            item.setProductionQuality(ProductionQuality.DAMAGED);
        }
        else {
            item.setProductionQuality(ProductionQuality.FLAWLESS);
        }
        return item.getProductionQuality();
    }

    public List<Item> getQualityCheckReport() {
        return Item.findByProductionQuality(ProductionQuality.DAMAGED);
    }

    private boolean itemIsDamaged(Item item) {
        return random.nextDouble(1.0) < QUALITY_CHECK_DAMAGED_POSSIBILITY;
    }
}
