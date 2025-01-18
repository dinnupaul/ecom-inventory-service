package com.ecom.inventoryservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "inventory", schema = "inventory_service")
public class Inventory {

    @Id
    @Column(name = "inventory_id", nullable = false, length = 50)
    private String inventoryId;
    @Column(name = "product_id", length = 50)
    private String productId;
    @Column(name = "quantity", length = 200)
    private Integer quantity;

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
