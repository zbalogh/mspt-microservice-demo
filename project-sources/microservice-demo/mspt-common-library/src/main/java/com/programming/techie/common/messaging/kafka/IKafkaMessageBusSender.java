package com.programming.techie.common.messaging.kafka;

import com.programming.techie.common.messaging.core.BaseMessage;
import com.programming.techie.common.messaging.core.IMessageBusSender;

public interface IKafkaMessageBusSender<T extends BaseMessage> extends IMessageBusSender<T> {
	
}
