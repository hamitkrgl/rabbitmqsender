package com.bilgeadam.rabbitmqSender.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanFactory {
	@Bean
	private RabbitTemplate setReturnCallback(ConnectionFactory connectionFactory) {
		RabbitTemplate myCustomTemplate = new RabbitTemplate(connectionFactory);
		ConfirmCallback confirmCallback = new ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (correlationData == null) {
					return;
				}
				/*
				 * This log is to be notified if the payload is succesfully delivered to
				 * rabbitmq It acks true even if the message goes to deadletter queue It doesn't
				 * return any data because we are not expecting a message from the consumer
				 */
				System.err.println("Correlation data: " + correlationData.toString());
				System.err.println("Ack: " + ack);
				System.err.println("Cause: " + cause);
				System.err.println();
			}
		};
		myCustomTemplate.setConfirmCallback(confirmCallback);
		return myCustomTemplate;
	}
}
