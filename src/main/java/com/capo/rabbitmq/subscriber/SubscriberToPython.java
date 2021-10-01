package com.capo.rabbitmq.subscriber;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.capo.rabbitmq.dto.SensorValues;

@Component
public class SubscriberToPython {
	
	@RabbitListener(queues="python.queue")
	public void consumeMessageFromQueue(SensorValues message) {
		System.out.println("message from rabbitmq ");
		System.out.println("temperatura "+message.getTemperatura());
		System.out.println("humedad "+message.getHumedad());
		System.out.println("timestamp "+message.getTimestamp());
	}
}
