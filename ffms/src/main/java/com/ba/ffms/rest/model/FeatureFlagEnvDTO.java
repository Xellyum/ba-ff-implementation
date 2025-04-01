package com.ba.ffms.rest.model;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record FeatureFlagEnvDTO(@NotNull Map<String, Boolean> development, @NotNull Map<String, Boolean> stage, @NotNull Map<String, Boolean> production) {
}
