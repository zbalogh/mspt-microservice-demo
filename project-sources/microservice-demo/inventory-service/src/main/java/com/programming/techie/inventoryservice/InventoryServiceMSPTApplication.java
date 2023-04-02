package com.programming.techie.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.programming.techie.inventoryservice.model.Inventory;
import com.programming.techie.inventoryservice.repository.InventoryRepository;

@SpringBootApplication
public class InventoryServiceMSPTApplication
{
	/*
	@Value(value = "${spring.datasource.url}")
    private String datasourceURL;
	
	@Value(value = "${spring.datasource.username}")
    private String postgresUsername;
	
	@Value(value = "${spring.datasource.password}")
    private String postgresPassword;
	*/
	
	/**
	 * The main entry point of the Spring Boot Application
	 */
    public static void main(String[] args)
    {
        SpringApplication.run(InventoryServiceMSPTApplication.class, args);
    }

    /**
     * This method is automatically executed when the Spring Boot Application has started up.
     */
    @Bean
    public CommandLineRunner createInventoryData(InventoryRepository inventoryRepository)
    {
        return args -> {
        	
        	String sku1 = "iphone_13";
        	String sku2 = "iphone_13_red";
        	
        	if (!inventoryRepository.findBySkuCode(sku1).isPresent())
        	{
        		Inventory inventory1 = new Inventory();
                inventory1.setSkuCode(sku1);
                inventory1.setQuantity(100);
                inventoryRepository.save(inventory1);
        	}
            
        	if (!inventoryRepository.findBySkuCode(sku2).isPresent())
        	{
	            Inventory inventory2 = new Inventory();
	            inventory2.setSkuCode(sku2);
	            inventory2.setQuantity(0);
	            inventoryRepository.save(inventory2);
        	}
        };
    }
    
}
