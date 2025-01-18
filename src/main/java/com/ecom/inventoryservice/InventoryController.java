package com.ecom.inventoryservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/inventory")
public class InventoryController
{
    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class.getName());
    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    Producer producer = new Producer();

    @PostMapping("/update") // URIs SERVE CHUNKS OF DATA UNLIKE URLs WHICH SERVE PAGES
    public ResponseEntity<String> updateInventoryDetails(@RequestBody Inventory inventory) throws JsonProcessingException {
        logger.info("initiating inventory update in Inventory Controller");
        inventoryRepository.save(inventory);
        logger.info(" inventory update completed successfully in inventory Table");
        logger.info(inventory.getInventoryId()," initiating inventory topic");
        producer.pubUpdateProductDetailsMessage(inventory.getInventoryId(), "INVENTORY DETAILS UPDATED SUCCESSFULLY");

        return ResponseEntity.ok("Details Updated Successfully");
    }

    //@GetMapping("get/restros")
    //public ResponseEntity<?> getRestros() throws InterruptedException
    //{
    //    Thread.sleep(1000);
     //   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

    //}

    @GetMapping("/getAll")
    public ResponseEntity<Inventory> getProduct(@PathVariable String id) {
        Inventory inventory = inventoryRepository.getReferenceById(id);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

                //new ResponseEntity(product.get(0));
                //product.map(ResponseEntity::ok)
               // .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
