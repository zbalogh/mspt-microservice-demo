package com.programming.techie.productservice.domain.appservice.inputports.service;

import java.util.List;

import com.programming.techie.productservice.domain.appservice.dto.ProductCreateRequest;
import com.programming.techie.productservice.domain.appservice.dto.ProductResponse;
import com.programming.techie.productservice.domain.appservice.dto.ProductUpdateRequest;

public interface ProductApplicationService {

public ProductResponse createProduct(ProductCreateRequest productRequest);
	
	public ProductResponse updateProduct(ProductUpdateRequest productUpdateRequest);
	
	public void deleteProduct(String id);

	public List<ProductResponse> getAllProducts();
	
	public ProductResponse getProductById(String id);
	
}
