package com.ba.ffms.persistence.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "feature_flag")
@Entity
public class FeatureFlag extends PanacheEntityBase {

    @Id
    @Column(name = "feature")
    public String feature;

    @Column(name = "active", nullable = false)
    public boolean active;

    @NotNull
    @Column(name = "environments", nullable = false)
    public String environments;

    @Column(name = "description")
    public String description;

    @NotNull
    @Column(name = "created_at", updatable = false, nullable = false)
    public Instant createdAt;

    @Column(name = "last_used")
    public Instant lastUsed;

    @Column(name = "dependencies")
    public String dependencies;

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Environment> getEnvironments() {
        return Arrays.stream(environments.split(",")).map(Environment::valueOf).toList();
    }

    public void setEnvironments(@NotNull List<Environment> environments) {
        this.environments = environments.stream().map(Environment::toString).collect(Collectors.joining(","));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Instant lastUsed) {
        this.lastUsed = lastUsed;
    }

    public List<String> getDependencies() {
        if (dependencies == null) {
            return null;
        }
        return Arrays.stream(dependencies.split(",")).toList();
    }

    public void setDependencies(List<String> dependencies) {
        if (dependencies == null) {
            this.dependencies = null;
            return;
        }
        this.dependencies = String.join(",", dependencies);
    }

    @Override
    public String toString() {
        return "FeatureFlag{" +
                "feature='" + feature + '\'' +
                ", active=" + active +
                ", environments='" + environments + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", lastUsed=" + lastUsed +
                ", dependencies='" + dependencies + '\'' +
                '}';
    }

    public static List<FeatureFlag> findByEnvironment(Environment environment) {
        return FeatureFlag.findAll().stream().map(ff -> (FeatureFlag)ff).filter(ff -> ff.getEnvironments().contains(environment)).toList();
    }

    public static List<FeatureFlag> findByFeature(String feature) {
        return list("feature", feature);
    }
}
