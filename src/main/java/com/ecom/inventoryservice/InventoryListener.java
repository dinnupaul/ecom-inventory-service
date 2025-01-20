package com.ecom.inventoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

//@Component
// @KafkaListener(topics = "order-events", groupId = "inventory-service")
public class InventoryListener
{
  /***  private final Logger logger = LoggerFactory.getLogger(InventoryListener.class);

    @Autowired
    ObjectMapper mapper;

    @Autowired
    InventoryController invController;

    public InventoryListener(KafkaTemplate<String, InventoryEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaHandler
    public void handleOrderEvent(String message) {
        try {
            // Deserialize JSON to OrderEvent
            OrderEvent orderEvent = mapper.readValue(message, OrderEvent.class);

            if ("ORDER_CREATED".equals(orderEvent.getEventType())) {
                // Check product availability
                invController.publishInventoryMessage(orderEvent.getRequest());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
} ***/



    /*** @Autowired
    ObjectMapper mapper;

    @KafkaListener(topics = "order-topic", groupId = "inventory-service")
    public void consumeOrderEvents(String message) throws IOException
    {
        //analytics_counter.increment();
        OrderEvent orderEvent =  mapper.readValue(message,OrderEvent.class);
        logger.info(String.format("#### -> order topic Consumed message -> %s", orderEvent.getOrderId()));
        if ("ORDER_CREATED".equals(orderEvent.getEventType())) {
            // Check product availability
            boolean isAvailable = checkProductAvailability(orderEvent.getRequest().getProductId());
            String status = isAvailable ? "INVENTORY_CONFIRMED" : "INVENTORY_FAILED";

            // Publish inventory response
            InventoryEvent inventoryEvent = new InventoryEvent(orderEvent.getOrderId(), status);
            kafkaTemplate.send("inventory-topic", orderEvent.getOrderId(), inventoryEvent);
        }
    }
    private boolean checkProductAvailability(String productId) {
        // Mock check for product availability
        return true; // Simulate available product
    }

    @KafkaListener(topics = "general-events", groupId = "1")
    public void consumeGenEvents(String message) throws IOException
    {
        //analytics_counter.increment();
        //ObjectMapper mapper  = new ObjectMapper();
        //Analytic datum =  mapper.readValue(message,Analytic.class);
        logger.info(String.format("#### -> Consumed message -> %s", message));
    } ***/

}




