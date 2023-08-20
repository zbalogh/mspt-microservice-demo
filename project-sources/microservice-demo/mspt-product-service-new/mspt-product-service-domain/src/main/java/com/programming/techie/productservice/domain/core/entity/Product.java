package com.programming.techie.productservice.domain.core.entity;

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
public class Product extends BaseDomainEntity<String>
{
	private static final long serialVersionUID = -9033058212918139397L;

	private String id;
	
	private String name;
	
	private String skuCode;
	
	private String description;
	
	private BigDecimal price;
	
}
