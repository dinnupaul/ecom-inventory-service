package com.ecom.inventoryservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/inventory")
public class InventoryController {
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class.getName());
    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    Producer producer = new Producer();

    private final RedisTemplate<String, Object> redisTemplate;

    public InventoryController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/update") // URIs SERVE CHUNKS OF DATA UNLIKE URLs WHICH SERVE PAGES
    public ResponseEntity<String> updateInventoryDetails(@RequestBody Inventory inventory) throws JsonProcessingException {
        logger.info("initiating inventory update in Inventory Controller");
        inventoryRepository.save(inventory);
        logger.info(" inventory update completed successfully in inventory Table");
        logger.info(inventory.getProductId(), " initiating inventory topic");
        producer.pubUpdateProductDetailsMessage(inventory.getProductId(), "INVENTORY DETAILS UPDATED SUCCESSFULLY");

        return ResponseEntity.ok("Details Updated Successfully");
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> checkInventory(@PathVariable String productId) {
        Optional<Inventory> inventory = inventoryRepository.findById(productId);
        return inventory.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public void checkAndPublishInventoryMessage(OrderRequest orderRequest,SagaState sagaState) throws JsonProcessingException {
         Inventory inventory = getProductAvailability(orderRequest.getProductId());
        boolean isAvailable = false;
         if (inventory.getQuantity() >= orderRequest.getQuantity()) {
            isAvailable = true;

            inventory.setQuantity(inventory.getQuantity() - orderRequest.getQuantity());
            inventoryRepository.save(inventory);
        }
        String status = isAvailable ? "INVENTORY_CONFIRMED" : "INVENTORY_FAILED";
        sagaState.updateStepStatus("Inventory", status);
        sagaState.setCurrentState(status);
        redisTemplate.opsForValue().set("ORDER_" + orderRequest.getOrderId(), sagaState);
       // if(isAvailable){
            // Publish inventory status event
            logger.info("Publish Inventory status event");
            producer.publishInventoryStatusMessage(orderRequest, status,sagaState);
        //}



     //   producer.publishOrderRetryMessage(orderRequest, status);
    }

    public Inventory getProductAvailability(String productId) {
        //Optional<Inventory> inventory = inventoryRepository.findById(productId);
        // return inventory.isPresent();
        return inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found")); // Simulate available product
    }

    public void rollbackInventory(String productId, int quantity) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        inventory.setQuantity(inventory.getQuantity() + quantity);
        inventoryRepository.save(inventory);
    }

    private boolean processPayment(String orderId) {
        // Mock payment processing
        return true; // Simulate successful payment
    }
}
