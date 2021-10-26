package com.capo.rabbitmq.configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {
	
	public static final String EXCHANGE_NAME="amq.topic";
	public static final String ROUTING_KEY_JAVA="java.blink.#";
	public static final String ROUTING_KEY_ARDUINO="arduino.#";
	public static final String ROUTING_KEY_PYTHON="python.#";
	public static final String QUEUE_NAME_JAVA="java.queue";
	public static final String QUEUE_NAME_ARDUINO="mqtt-subscription-Arduinoqos0";
	public static final String QUEUE_NAME_PYTHON="python.queue";
	public static final boolean IS_DURABLE_QUEUE=false;
	
	
	@Bean
	public Queue queueJava() {
		return new Queue(QUEUE_NAME_JAVA,IS_DURABLE_QUEUE);
	}
	
	@Bean
	public Queue queueFromArduino() {
		return new Queue(QUEUE_NAME_PYTHON,IS_DURABLE_QUEUE);
	}
	
	/*
	@Bean
	public Queue queueToArduino() {
		return new Queue(QUEUE_NAME_ARDUINO,IS_DURABLE_QUEUE);
	}*/
	
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}
	
	@Bean
	public Binding bindingJava(@Qualifier("queueJava") Queue queue, TopicExchange topicExchange) {
		return BindingBuilder.bind(queue).to(topicExchange).with(ROUTING_KEY_JAVA);
	}
	
	@Bean
	public Binding bindingFromArduino(@Qualifier("queueFromArduino") Queue queue, TopicExchange topicExchange) {
		return BindingBuilder.bind(queue).to(topicExchange).with(ROUTING_KEY_PYTHON);
	}
	
	/*
	@Bean
	public Binding bindingToArduino(@Qualifier("queueToArduino") Queue queue, TopicExchange topicExchange) {
		return BindingBuilder.bind(queue).to(topicExchange).with(ROUTING_KEY_ARDUINO);
	}*/
	
	@Bean
	public MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	@Bean
	public AmqpTemplate template(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate= new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter());
		return rabbitTemplate; 
	}
}
