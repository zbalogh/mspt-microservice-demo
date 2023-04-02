package com.programming.techie.productservice.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {

	@Id
	private String id;
	
	@Field(name = "name")
	private String name;
	
	@Field(name = "sku_code")
	private String skuCode;
	
	private String description;
	
	private BigDecimal price;

}
