package com.ecom.inventoryservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Random;

@Service
public class Producer
{
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC = "inventory-topic";

    @Autowired //DEPENDENCY INJECTION PROMISE FULFILLED AT RUNTIME
    private KafkaTemplate<String, String> kafkaTemplate ;

    @Autowired
    ObjectMapper objectMapper;

    public void pubUpdateProductDetailsMessage(String principal,
                                            String description) throws JsonProcessingException // LOGIN | REGISTER
    {
        Analytic analytic = new Analytic();
        analytic.setObjectid(String.valueOf((new Random()).nextInt()));
        analytic.setType("UPDATE");
        analytic.setPrincipal(principal);
        analytic.setDescription(description);
        analytic.setTimestamp(LocalTime.now()); // SETTING THE TIMESTAMP OF THE MESSAGE

        // convert to JSON
        String datum =  objectMapper.writeValueAsString(analytic);

        logger.info(String.format("#### -> Producing message -> %s", datum));
        this.kafkaTemplate.send(TOPIC,datum);
    }


    /***public void publishInventoryMessage(OrderRequest request) throws JsonProcessingException {
        OrderEvent event = new OrderEvent(request.getOrderId(), "ORDER_CREATED", request);
        String datum =  objectMapper.writeValueAsString(event);
        kafkaTemplate.send("order-topic", request.getOrderId(), datum);


    }***/

    public void publishInventoryStatusMessage(OrderRequest request, String inventoryStatus, SagaState sagaState) throws JsonProcessingException {
        // Publish inventory response
        InventoryEvent inventoryEvent = new InventoryEvent(request.getOrderId(), inventoryStatus, request,sagaState);
        String inventoryEventJson = objectMapper.writeValueAsString(inventoryEvent);
        kafkaTemplate.send("inventory-topic", request.getOrderId(), inventoryEventJson);
    }

    public void publishOrderRetryMessage(OrderRequest request, String inventoryStatus) throws JsonProcessingException {
        // Publish inventory response
        InterimEvent interimEvent = new InterimEvent(request.getOrderId(), "Inventory", inventoryStatus);
        String interimEventJson = objectMapper.writeValueAsString(interimEvent);
        kafkaTemplate.send("order-retry-topic", request.getOrderId(), interimEventJson);
    }

}
