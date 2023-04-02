package com.programming.techie.cartservice.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "ShoppingCart", timeToLive = 1800L)	// TTL = 1800 seconds (30 minutes)
public class ShoppingCart implements Serializable {
	
	private static final long serialVersionUID = 6813975630781996706L;

	@Id
	private String userid;

	private Date lastUpdatedAt;
	
	private List<ShoppingCartItem> cartItems;

}
