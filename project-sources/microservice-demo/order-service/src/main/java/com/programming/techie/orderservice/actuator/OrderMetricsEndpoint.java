package com.programming.techie.orderservice.actuator;

import java.util.function.Supplier;

import javax.annotation.PostConstruct;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import com.programming.techie.orderservice.repository.OrderRepository;

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
@Endpoint(id = "order")
@RequiredArgsConstructor
@Slf4j
public class OrderMetricsEndpoint {

	private final OrderRepository orderRepository;
	
	private final MeterRegistry meterRegistry;
	
	@PostConstruct
	private void init()
	{
		// register a micrometer metrics which is used by Prometheus
		Gauge.builder("mspt.order.api.totalOrders", fetchTotalOrderCount())
        .description("Number of total orders in the database.")
        .register(meterRegistry);
	}
	
	/**
	 * Supplier method used by Micrometer metrics to fetch the total amount of orders.
	 */
	private Supplier<Number> fetchTotalOrderCount()
	{
	    return () -> {
	    	long total = orderRepository.count();
	    	log.info("Prometheus metrics for number of total orders in the database: " + total);
	    	return total;
	    };
	}
	
	@ReadOperation
    public NumberOfOrders numberOfOrders()
	{
		// provide actuator data for the number of total orders
		long total = orderRepository.count();
		log.info("Actuator metrics for number of total orders in the database: " + total);
        return new NumberOfOrders(total);
    }
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	private class NumberOfOrders
	{
		private long totalOrders;
	}
	
}
