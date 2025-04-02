package com.ba.pws.rest.model;

import jakarta.validation.constraints.NotNull;

public record FeatureFlagStateDTO(@NotNull String feature, boolean active) {
}
