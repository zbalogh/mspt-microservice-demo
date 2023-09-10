package com.programming.techie.orderservice.domain.core.entity;

import java.math.BigDecimal;

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
public class OrderLineItems extends BaseDomainEntity<Long> {

	private static final long serialVersionUID = 2576841373157951279L;

	private Long id;
	
	private String skuCode;
	
	private BigDecimal price;
	
	private Integer quantity;
	
}
