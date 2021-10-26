package com.capo.rabbitmq.configuration;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.capo.rabbitmq.service.MqttConfigurationService;

@Service
public class MqttConfigurationArduino implements MqttConfigurationService  {
	
	public static final String ROUTING_KEY_ARDUINO="arduino.topic";
	private static final String HOST="tcp://192.168.0.95:1883";
	private static final String CLIENT_ID="Arduino";
	
	@Value("${spring.rabbitmq.username}")
	private String userName;
	
	@Value("${spring.rabbitmq.password}")
	private String password;
	
	
	@Override
	public void publish(String message) {
		try {
			MqttAsyncClient client= connection();
			MqttMessage mqttMessage= setMessage(message,0,false);
			client.publish(ROUTING_KEY_ARDUINO, mqttMessage);
			//client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	private MqttAsyncClient connection() {
		MqttAsyncClient mqttClient=null;
		try {
			mqttClient= new MqttAsyncClient(HOST,CLIENT_ID);
			MqttConnectOptions options= new MqttConnectOptions();
			options.setUserName(userName);
			options.setPassword(password.toCharArray());
			//options.setAutomaticReconnect(true);
			options.setCleanSession(false);
			options.setConnectionTimeout(300);
			options.setKeepAliveInterval(45);
			mqttClient.connect(options);
		} catch (MqttException e) {
			e.printStackTrace(); 
		}
		return mqttClient;
	}
	
	private MqttMessage setMessage(String payload,int qos,boolean retained) {
		MqttMessage mqttMessage= new MqttMessage();
		mqttMessage.setPayload(payload.getBytes());
		mqttMessage.setQos(qos);
		mqttMessage.setRetained(retained);
		return mqttMessage;
	}
}
