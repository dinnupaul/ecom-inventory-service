package com.ecom.inventoryservice;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.client.ReactorResourceFactory;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @PreDestroy
    public void cleanUp() {
        ReactorResourceFactory resourceFactory = new ReactorResourceFactory();
        resourceFactory.destroy();
    }

}
