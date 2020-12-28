package com.test.bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;

import com.test.rmq.HomeController;


public class RabbitMQInstance {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private CachingConnectionFactory connectionFactory;
	private Queue logQ;
	
	@PostConstruct 
	public void Init(){
		connectionFactory = new CachingConnectionFactory();	
		AmqpAdmin admin = new RabbitAdmin(connectionFactory);
		logger.info("Connection to rabbitmq done!");
		logQ = admin.declareQueue();
		admin.declareBinding(new Binding(logQ.getName(), DestinationType.QUEUE, "amq.rabbitmq.log", "#", null) );
		logger.info("Queue Decraled!");
	}

	public Queue getBindQueue(){
		return logQ;
	}
	
	public ConnectionFactory getConncetionFactory(){
		return connectionFactory;
	}
	
	@PreDestroy
	public void Deinit(){
		connectionFactory.destroy();
		logger.info("Connection Factory Destroied");
	}
	
}
