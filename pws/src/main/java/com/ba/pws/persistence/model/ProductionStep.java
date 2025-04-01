package com.ba.pws.persistence.model;

public enum ProductionStep {
    BACKLOG,
    PLANNING,
    ASSEMBLY,
    QUALITY_CHECK,
    PACKAGING,
    READY_TO_SHIP;

    public ProductionStep nextStep() {
        if (ProductionStep.READY_TO_SHIP.equals(this)) {
            return READY_TO_SHIP;
        }

        return ProductionStep.values()[this.ordinal() + 1];
    }
}
