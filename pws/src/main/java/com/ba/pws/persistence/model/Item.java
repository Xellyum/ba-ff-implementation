package com.ba.pws.persistence.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "item")
public class Item extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private long itemId;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType type;

    @Column(name = "production_quality")
    @Enumerated(EnumType.STRING)
    private ProductionQuality productionQuality;

    @NotNull
    @Column(name = "production_step", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductionStep productionStep = ProductionStep.BACKLOG;

    @Column(name = "priority")
    private Long priority;

    //@NotNull
    //@ManyToOne
    //@JoinColumn(name = "order_id", nullable = false)
    //private Order order;

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

     */

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public ProductionQuality getProductionQuality() {
        return productionQuality;
    }

    public void setProductionQuality(ProductionQuality productionQuality) {
        this.productionQuality = productionQuality;
    }

    public ProductionStep getProductionStep() {
        return productionStep;
    }

    public void setProductionStep(ProductionStep productionStep) {
        this.productionStep = productionStep;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public static List<Item> findByProductionStep(ProductionStep productionStep) {
        return Item.find("productionStep", productionStep).list();
    }

    public static List<Item> findByProductionQuality(ProductionQuality productionQuality) {
        return Item.find("productionQuality", productionQuality).list();
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", priority=" + priority +
                ", productionQuality=" + productionQuality +
                ", productionStep=" + productionStep +
                //", orderId=" + order.getOrderId() +
                '}';
    }
}
