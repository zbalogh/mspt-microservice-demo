package com.programming.techie.productservice.domain.appservice.outputports.repository;

import java.util.List;
import java.util.Optional;

import com.programming.techie.productservice.domain.core.entity.Product;

public interface ProductDataAccess
{
	public Product create(Product product);
	
	public Product update(Product product);
	
	public Optional<Product> findById(String id);
	
	public void delete(String id);
	
	public void delete(Product product);
	
	public List<Product> findAll();
	
}
