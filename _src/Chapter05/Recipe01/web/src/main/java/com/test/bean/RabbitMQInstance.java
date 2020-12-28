package com.test.bean;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 
 * @author gabriele
 *
 * Check out the root-context.xml file to change the parameters.
 */

public class RabbitMQInstance {
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQInstance.class);

	ConnectionFactory factory = new ConnectionFactory();
	Connection connection = null;
	private String rabbitMQhost;// <property name="rabbitMQhost" value="localhost"/> in root-context.xml

	private String lastError="";
	public String getLastError() {
		return lastError;
	}


	public Connection getConnection() {
		return connection;
	}


	public void setConnection(Connection connection) {
		this.connection = connection;
	}


	@PostConstruct   
	public void Init(){
		lastError ="";
		factory.setHost(rabbitMQhost);
		try {
			logger.info("Connection to RabbitMQ..."+  rabbitMQhost);
			connection = factory.newConnection();
			logger.info("Connected!");
		} catch (IOException e) {
			lastError ="Error during Rabbitmq connection: "+ e.getMessage();
			logger.error(e.getMessage());
		}
	}


	@PreDestroy
	public void Deinit(){
		try{
			connection.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}


	public String getRabbitMQhost() {
		return rabbitMQhost;
	}

	public void setRabbitMQhost(String rabbitMQhost) {
		this.rabbitMQhost = rabbitMQhost;
	}
}
