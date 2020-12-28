package rmqexample;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="13"; 
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR +".  Handling unroutable messages **");
		String RabbitmqHost = "localhost";
		if (args.length > 0)
			RabbitmqHost = args[0];
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
		try 
		{
			Connection connection = factory.newConnection();
			System.out.println("Connected: " + RabbitmqHost);
			Channel channel = connection.createChannel();
			
			String myExchange = "myUnroutableExchange_"+RECIPE_NR;
			channel.exchangeDeclare(myExchange, "direct", false, false, null);	
			
			String myPrivateQueue = channel.queueDeclare().getQueue();
			channel.queueBind(myPrivateQueue,myExchange, "");
					
			ActualConsumer consumer = new ActualConsumer(channel);
		    boolean _autoAck = true; 
			String consumerTag = channel.basicConsume(myPrivateQueue, _autoAck, consumer);
			
			System.out.println("Consumer Ready - press a key to terminate");
			System.in.read();
			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

}
