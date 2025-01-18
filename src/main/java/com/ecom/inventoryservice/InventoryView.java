package com.ecom.inventoryservice;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InventoryView
{
    String inventoryId;
    String productId;
    Integer quantity;
}
