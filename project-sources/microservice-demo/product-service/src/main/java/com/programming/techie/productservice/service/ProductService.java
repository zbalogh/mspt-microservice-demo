package com.programming.techie.productservice.service;

import java.util.List;

import com.programming.techie.productservice.dto.ProductRequest;
import com.programming.techie.productservice.dto.ProductResponse;
import com.programming.techie.productservice.dto.ProductUpdateRequest;

public interface ProductService {
	
	public ProductResponse createProduct(ProductRequest productRequest);
	
	public ProductResponse updateProduct(ProductUpdateRequest productUpdateRequest);
	
	public void deleteProduct(String id);

	public List<ProductResponse> getAllProducts();
	
	public ProductResponse getProductById(String id);

}
