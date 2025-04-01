package com.ba.pws.persistence.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "resource_inventory")
public class ResourceInventory extends PanacheEntityBase {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "resource")
    private ResourceType resource;

    @Column(name = "max_stock", nullable = false)
    private int maxStock = 1000;

    @Column(name = "current_stock", nullable = false)
    private int currentStock = 250;

    @Column(name = "recently_used", nullable = false)
    private int recentlyUsed = 0;

    public ResourceType getResource() {
        return resource;
    }

    public void setResource(ResourceType resource) {
        this.resource = resource;
    }

    public int getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(int maxStock) {
        this.maxStock = maxStock;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getRecentlyUsed() {
        return recentlyUsed;
    }

    public void setRecentlyUsed(int recentlyUsed) {
        this.recentlyUsed = recentlyUsed;
    }

    @Override
    public String toString() {
        return "ResourceInventory{" +
                "resource='" + resource + '\'' +
                ", maxStock=" + maxStock +
                ", currentStock=" + currentStock +
                ", recentlyUsed=" + recentlyUsed +
                '}';
    }
}
