package com.bilgeadam.rabbitmqSender.controller;

import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bilgeadam.rabbitmqSender.dto.Person;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class RabbitController {

	private RabbitTemplate rabbitTemplate;

	@PostConstruct
	public void postOps() {
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
	}

	@GetMapping(path = "helloRabbitMQ")
	public ResponseEntity<String> helloRabbitMQ() {
		// localhost:8080/helloRabbitMQ
		Message mesaj = MessageBuilder.withBody("merhaba rabbitmq from spring".getBytes()).build();
		rabbitTemplate.send("amq.direct", "rabbitDirectRKey", mesaj);
		return ResponseEntity.ok("Message queue 'ya mesaj gönderildi");
	}

	@GetMapping(path = "helloRabbitMQData")
	public ResponseEntity<String> helloRabbitMQData() {
		// localhost:8080/helloRabbitMQData
		Person person = new Person(1, "Hamit");
		String id = new Random().nextInt(100000) + "-" + new Random().nextInt(100000) + "-"
				+ new Random().nextInt(100000) + "-";
		CorrelationData correlationData = new CorrelationData(id);
		// rabbitTemplate.convertSendAndReceive("amq.direct", "rabbitDirectRKeyc",
		// person, correlationData);
		rabbitTemplate.convertAndSend("amq.direct", "rabbitDirectRKey", person, correlationData);
		return ResponseEntity.ok("Message queue 'ya person datası gönderildi");
	}

	@GetMapping(path = "helloRabbitMQTopic")
	public ResponseEntity<String> helloRabbitMQTopic() {
		// localhost:8080/helloRabbitMQTopic
		Message mesaj = MessageBuilder.withBody("merhaba rabbitmq normal mesaj".getBytes()).build();
		rabbitTemplate.send("amq.topic", "mail.normal", mesaj);
		mesaj = MessageBuilder.withBody("merhaba rabbitmq özel mesaj".getBytes()).build();
		rabbitTemplate.send("amq.topic", "mail.özel", mesaj);
		// email.*
		mesaj = MessageBuilder.withBody("merhaba rabbitmq normal mesaj".getBytes()).build();
		rabbitTemplate.send("amq.topic", "email.özel", mesaj);
		mesaj = MessageBuilder.withBody("merhaba rabbitmq özel mesaj".getBytes()).build();
		rabbitTemplate.send("amq.topic", "email.özel", mesaj);
		return ResponseEntity.ok("Message queue 'ya mesaj gönderildi");
	}
}
