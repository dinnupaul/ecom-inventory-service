package com.ecom.inventoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer
{
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);


    @Autowired
    ObjectMapper mapper;

    @Autowired
    InventoryController invController;

    @KafkaListener(topics = "order-topic", groupId = "ecom-inventory-service")
    public void consumeOrderEvents(String message) throws IOException
    {
        //analytics_counter.increment();
        //Analytic datum =  mapper.readValue(message,Analytic.class);
        logger.info(String.format("#### -> about to consume order topic"));
        OrderEvent orderEvent = mapper.readValue(message, OrderEvent.class);
        logger.info(String.format("#### -> Consumed message order topic-> %s", orderEvent.getOrderId()));

        if ("ORDER_CREATED".equals(orderEvent.getEventType())) {
            // Check product availability
            invController.checkAndPublishInventoryMessage(orderEvent.getRequest(),orderEvent.getSagaState());
        }
    }

    @KafkaListener(topics = "general-events", groupId = "1")
    public void consumeGenEvents(String message) throws IOException
    {
        //analytics_counter.increment();
        //ObjectMapper mapper  = new ObjectMapper();
        //Analytic datum =  mapper.readValue(message,Analytic.class);
        logger.info(String.format("#### -> Consumed message -> %s", message));
    }

    @KafkaListener(topics = "product-topic", groupId = "1")
    public void consumeProductTopic(String message) throws IOException
    {
        //analytics_counter.increment();
        //ObjectMapper mapper  = new ObjectMapper();
        //Analytic datum =  mapper.readValue(message,Analytic.class);
        logger.info(String.format("#### -> Consumed message -> %s", message));
    }


}

