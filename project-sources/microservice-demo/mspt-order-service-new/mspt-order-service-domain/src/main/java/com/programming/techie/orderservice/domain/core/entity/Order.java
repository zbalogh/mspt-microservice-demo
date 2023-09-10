package com.programming.techie.orderservice.domain.core.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.programming.techie.common.domain.entity.BaseDomainEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Order extends BaseDomainEntity<Long> {

	private static final long serialVersionUID = -3182878922980758195L;

	private Long id;
	
	private String orderNumber;
	
	@Builder.Default
	private Date orderCreatedAt = new Date();
	
	private String userid;
	
	private List<OrderLineItems> orderLineItemsList;
	
	
	public void initializeOrder()
	{
		// generate a unique order number/identifier
		this.orderNumber = UUID.randomUUID().toString();
		// set the order created time
		this.orderCreatedAt = new Date();
	}
	
	public void validateOrder()
	{
		if (userid == null || userid.isEmpty()) {
			throw new IllegalArgumentException("Order must be associated with a user.");
		}
		
		if (orderLineItemsList == null || orderLineItemsList.isEmpty()) {
			throw new IllegalArgumentException("Order must contain at least one item.");
		}
		
		orderLineItemsList.forEach(item -> {
			if (item == null) {
				throw new IllegalArgumentException("Order item is null.");
			}
			if (item.getQuantity() <= 0) {
				throw new IllegalArgumentException("Order item quantity is invalid.");
			}
			if (item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
				throw new IllegalArgumentException("Order item price is invalid.");
			}
		});
	}
	
}
