package com.programming.techie.orderservice.domain.core.events;

import com.programming.techie.common.messaging.core.BaseMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPlacedEvent extends BaseMessage {
	
	private String orderNumber;
	
	private String userid;
	
}
