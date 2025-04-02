package com.ba.pws.featureflag.model;

public enum FeatureFlag {
    PRIORITY_QUEUE_ENABLED("priority-queue-enabled"),
    QUALITY_CHECK_ENABLED("quality-check-enabled"),
    RESOURCE_MANAGEMENT_ENABLED("resource-management-enabled");

    private final String value;

    FeatureFlag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
