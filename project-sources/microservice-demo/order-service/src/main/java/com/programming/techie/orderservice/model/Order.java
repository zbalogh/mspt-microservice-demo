package com.programming.techie.orderservice.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_orders")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Order
{
	//@SequenceGenerator(name = "t_orders_id_seq", sequenceName = "t_orders_id_seq", allocationSize=1, initialValue=1)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_orders_id_seq")
	//@Column(name = "id", columnDefinition="bigint DEFAULT nextval('t_orders_id_seq')", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "SERIAL", nullable = false)
	@Id
	private Long id;
	
	@Column(name = "order_number", nullable = false) 
	private String orderNumber;
	
	@Builder.Default
	@Column(name = "order_created")
	private Date orderCreatedAt = new Date();
	
	@Column
	private String userid;
	
	// FetchType.EAGER: We always fetch the details (order items) for this order
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<OrderLineItems> orderLineItemsList;

}
