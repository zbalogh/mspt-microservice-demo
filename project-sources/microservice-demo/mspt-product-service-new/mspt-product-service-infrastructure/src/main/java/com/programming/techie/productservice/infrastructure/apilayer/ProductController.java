package com.programming.techie.productservice.infrastructure.apilayer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.programming.techie.common.jwt.JwtUtil;
import com.programming.techie.productservice.domain.appservice.dto.ProductCreateRequest;
import com.programming.techie.productservice.domain.appservice.dto.ProductResponse;
import com.programming.techie.productservice.domain.appservice.dto.ProductUpdateRequest;
import com.programming.techie.productservice.domain.appservice.inputports.service.ProductApplicationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

	// Injected via constructor
	private final ProductApplicationService productService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponse createProduct(@RequestBody ProductCreateRequest productRequest)
	{
		return productService.createProduct(productRequest);
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	public ProductResponse updateProduct(@RequestBody ProductUpdateRequest productUpdateRequest)
	{
		return productService.updateProduct(productUpdateRequest);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteProduct(@PathVariable String id)
	{
		productService.deleteProduct(id);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<ProductResponse> getAllProducts(HttpServletRequest request)
	{
		String userNameFromToken = JwtUtil.getUserNameFromBearerToken(request);
		log.info("Username from the bearer token: " + userNameFromToken);
		
		return productService.getAllProducts();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProductResponse getProductById(@PathVariable String id)
	{
		return productService.getProductById(id);
	}
	
}
