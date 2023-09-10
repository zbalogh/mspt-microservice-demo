package com.programming.techie.orderservice.dataaccess.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_order_line_items")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderLineItemsEntity
{
	//@SequenceGenerator(name = "t_order_line_items_id_seq", sequenceName = "t_order_line_items_id_seq", allocationSize=1, initialValue=1)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_order_line_items_id_seq")
	//@Column(name = "id", columnDefinition="bigint DEFAULT nextval('t_order_line_items_id_seq')", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "SERIAL", nullable = false)
	@Id
	private Long id;
	
	@Column(name = "sku_code")
	private String skuCode;
	
	@Column
	private BigDecimal price;
	
	@Column
	private Integer quantity;
	
	@ManyToOne
	@JoinColumn(name = "order_id", columnDefinition = "BIGINT", nullable = false)
	private OrderEntity order;
	
}
