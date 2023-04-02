package com.programming.techie.notificationservice.service;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.programming.techie.notificationservice.messaging.events.OrderPlacedEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderPlacedEventProcessor {
	
	// these variables are initialized inside the constructor
	private final int EXECUTOR_CORE_POOL_SIZE;
	private final int EXECUTOR_MAXIMUM_POOL_SIZE;
	private final long EXECUTOR_KEEP_ALIVE_TIME;
	
	// executor used for processing the incoming order events
	private final Executor orderPlacedEventExecutor;
	
	/**
	 * Constructor
	 */
	public OrderPlacedEventProcessor()
	{
		// initialize variables for executor
		int availProcessors = Runtime.getRuntime().availableProcessors();
		EXECUTOR_CORE_POOL_SIZE = availProcessors;
		EXECUTOR_MAXIMUM_POOL_SIZE = availProcessors * 4;
		EXECUTOR_KEEP_ALIVE_TIME = 120;
		
		// create executor for this event processor
		this.orderPlacedEventExecutor = orderEventPlacedThreadPoolExecutor();
	}
	
	/**
	 * Create Thread Pool Executor
	 */
	private Executor orderEventPlacedThreadPoolExecutor()
	{
		// thread pool executor
		return new ThreadPoolExecutor(
				EXECUTOR_CORE_POOL_SIZE, 
				EXECUTOR_MAXIMUM_POOL_SIZE, 
				EXECUTOR_KEEP_ALIVE_TIME, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(),
				new ThreadPoolExecutor.AbortPolicy()
		);
	}
	
	/*
	private Executor orderEventPlacedSingleThreadExecutor()
	{
		// single thread executor
		return Executors.newSingleThreadExecutor();
	}
	
	private Executor orderEventPlacedThreadPoolTaskExecutor()
	{
		// thread pool task executor provided by Spring Framework
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(EXECUTOR_CORE_POOL_SIZE);
		executor.setMaxPoolSize(EXECUTOR_MAXIMUM_POOL_SIZE);
		//executor.setQueueCapacity(5000);
		executor.setThreadNamePrefix("OrderProcessor-");
		executor.initialize();
		return executor;
	}
	*/

	public void submit(OrderPlacedEvent orderPlacedEvent)
	{
		// in real application we would send out an email notification about the order.
    	// we just write a log message about the received notification.
    	
		log.info("Received the incoming OrderPlacedEvent: OrderNumber={} [MessageCreated={}, MessageId={}] by userid={}", orderPlacedEvent.getOrderNumber(), orderPlacedEvent.getMessageCreated(), orderPlacedEvent.getMessageIdentifier(), orderPlacedEvent.getUserid());
		
		// execute event processor
		this.orderPlacedEventExecutor.execute( new OrderPlacedEventProcessorRunner(orderPlacedEvent) );
	}
	
	/**
	 * Event processor function.
	 */
	private void processEvent(OrderPlacedEvent orderPlacedEvent)
	{
		log.info("Processing the incoming OrderPlacedEvent: OrderNumber={} [MessageCreated={}, MessageId={}] by userid={}", orderPlacedEvent.getOrderNumber(), orderPlacedEvent.getMessageCreated(), orderPlacedEvent.getMessageIdentifier(), orderPlacedEvent.getUserid());
	}
	

	/**
	 * Inner class for runnable to execute task for processing event.
	 */
	private class OrderPlacedEventProcessorRunner implements Runnable
	{
		private final OrderPlacedEvent orderPlacedEvent;
		
		public OrderPlacedEventProcessorRunner(OrderPlacedEvent orderPlacedEvent)
		{
			this.orderPlacedEvent = orderPlacedEvent;
		}
		
		@Override
		public void run()
		{
			processEvent(orderPlacedEvent);
		}
	}
	
}
