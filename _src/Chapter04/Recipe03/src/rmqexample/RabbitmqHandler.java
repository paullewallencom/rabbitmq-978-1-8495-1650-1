package rmqexample;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitmqHandler {
	
	private final String host = "184.73.245.85";
	private final String username = "guest";
	private final String password = "guest";
	private String consumerTag;
	
	private ConnectionFactory factory;
	private Connection connection;
	private Channel channel;
	
	public void Connect() throws IOException {
		factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setUsername(username);
		factory.setPassword(password);
		connection = factory.newConnection();
		channel = connection.createChannel();
		
		String myQueue = channel.queueDeclare().getQueue();
		channel.queueBind(myQueue, "amq.fanout", "");
		ActualConsumer consumer = new ActualConsumer(channel);
		boolean autoAck = true; 
		consumerTag = channel.basicConsume(myQueue, autoAck, consumer);
	}
	
	public void Disconnect() throws IOException {
		channel.basicCancel(consumerTag);
		channel.close();
		connection.close();
	}
	
}
