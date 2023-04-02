package com.programming.techie.orderservice.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
	
	private String userid;
	
	private List<OrderLineItemsDto> orderLineItemsDtoList;

}
