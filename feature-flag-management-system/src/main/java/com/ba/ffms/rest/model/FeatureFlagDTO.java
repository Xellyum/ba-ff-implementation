package com.ba.ffms.rest.model;

import com.ba.ffms.persistence.model.Environment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.List;

public record FeatureFlagDTO(@NotNull String feature, @NotNull Boolean active, @NotEmpty List<Environment> environments, String description, Instant createdAt, Instant lastUsed, List<String> dependencies) {
}
