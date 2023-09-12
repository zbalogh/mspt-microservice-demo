package com.programming.techie.orderservice.domain.appservice.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
	
	private Long id;
	
	private String orderNumber;
	
	private Date orderCreatedAt;

	private String userid;
	
	private List<OrderLineItemsDto> orderLineItemsDtoList;
	
}
