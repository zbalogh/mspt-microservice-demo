package com.programming.techie.notificationservice.messaging.core;

public interface IMessageBusSender<T extends BaseMessage> {
	
	public abstract void publishMessage(String queueOrTopicName, T message);
	
	public abstract void publishMessage(T message);
	
}
