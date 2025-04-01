package com.ba.nc.persistence.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "feature_flag")
public class FeatureFlag extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "feature_flag_config", nullable = false)
    private String featureFlagConfig;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() {
        return id;
    }

    public Map<String, Boolean> getFeatureFlagConfig() {
        try {
            return new ObjectMapper().readValue(featureFlagConfig, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    public void setFeatureFlagConfig(Map<String, Boolean> featureFlagConfig) {
        try {
            this.featureFlagConfig = new ObjectMapper().writeValueAsString(featureFlagConfig);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize JSON", e);
        }
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant timestamp) {
        this.createdAt = timestamp;
    }

    @Override
    public String toString() {
        return "FeatureFlag{" +
                "id=" + id +
                ", featureFlagConfig='" + featureFlagConfig + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public static FeatureFlag findMostRecent() {
        return findAll(Sort.by("createdAt")).firstResult();
    }
}
