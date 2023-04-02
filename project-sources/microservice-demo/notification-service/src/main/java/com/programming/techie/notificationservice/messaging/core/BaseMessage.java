package com.programming.techie.notificationservice.messaging.core;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseMessage {
	
	protected String messageIdentifier;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	protected Date messageCreated;

	public BaseMessage()
	{
		this.messageIdentifier = UUID.randomUUID().toString();
		this.messageCreated = new Date();
	}
}
