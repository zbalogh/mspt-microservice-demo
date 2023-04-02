package com.programming.techie.orderservice.messaging.kafka;

import com.programming.techie.orderservice.messaging.core.BaseMessage;
import com.programming.techie.orderservice.messaging.core.IMessageBusSender;

public interface IKafkaMessageBusSender<T extends BaseMessage> extends IMessageBusSender<T> {
	
}
