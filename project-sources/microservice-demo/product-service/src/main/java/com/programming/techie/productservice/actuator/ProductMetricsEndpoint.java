package com.programming.techie.productservice.actuator;

import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import com.programming.techie.productservice.repository.ProductRepository;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


// https://autsoft.net/defining-custom-metrics-in-a-spring-boot-application-using-micrometer
//
// https://fabianlee.org/2022/06/29/java-adding-custom-metrics-to-spring-boot-micrometer-prometheus-endpoint
//
// https://www.baeldung.com/spring-boot-actuators

@Component
@Endpoint(id = "product")
@RequiredArgsConstructor
@Slf4j
public class ProductMetricsEndpoint {

	private final ProductRepository productRepository;
	
	private final MeterRegistry meterRegistry;
	
	@PostConstruct
	private void init()
	{
		// register a micrometer metrics which is used by Prometheus
		Gauge.builder("mspt.product.api.totalProducts", fetchTotalProductCount())
        .description("Number of total products in the database.")
        .register(meterRegistry);
	}
	
	/**
	 * Supplier method used by Micrometer metrics to fetch the total amount of products.
	 */
	private Supplier<Number> fetchTotalProductCount()
	{
	    return () -> {
	    	int numProducts = productRepository.findAll().size();
	    	log.info("Prometheus metrics for number of total products in the database: " + numProducts);
	    	return numProducts;
	    };
	}
	
	@ReadOperation
    public NumberOfProducts numberOfProducts()
	{
		// provide actuator data for the number of total products
		int numProducts = productRepository.findAll().size();
		log.info("Actuator metrics for number of total products in the database: " + numProducts);
        return new NumberOfProducts(numProducts);
    }
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	private class NumberOfProducts
	{
		private int totalProducts;
	}
	
}
