package com.programming.techie.notificationservice.messaging.events;

import com.programming.techie.notificationservice.messaging.core.BaseMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPlacedEvent extends BaseMessage {

	private String orderNumber;
	
	private String userid;
	
}
