package com.capo.rabbitmq.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capo.rabbitmq.configuration.RabbitmqConfiguration;
import com.capo.rabbitmq.dto.BlinkNumber;
import com.capo.rabbitmq.dto.SensorValues;

@RestController
@RequestMapping("/python")
public class Controller {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@PostMapping("/blinkArduino")
	public String responseSensorValues(@RequestBody SensorValues sensorValues) {
		rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_NAME,RabbitmqConfiguration.ROUTING_KEY_PYTHON,sensorValues);
		return "Success response to sensor";
	}
	
	@PostMapping("/blinkJava")
	public String setBlinkLedArduino(@RequestBody BlinkNumber blinkNUmber) {
		rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_NAME,RabbitmqConfiguration.ROUTING_KEY_JAVA,blinkNUmber);
		return "The number blink was set";
	}
}
