package rmqexample;
/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 *
 */
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {

	/**
	 * @param args
	 */
	
	
	//http://www.rabbitmq.com/releases/rabbitmq-dotnet-client/v2.3.1/rabbitmq-dotnet-client-2.3.1-client-htmldoc/html/type-RabbitMQ.Client.IBasicProperties.html
	public static void main(String[] args) {
		String RECIPE_NR="11"; 
		System.out.println(" ** RabbitmqCookBook - Recipe number "+	RECIPE_NR+". Using a basic properties message **");
		String RabbitmqHost = "localhost";
		if (args.length > 0)
			RabbitmqHost = args[0];
		

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
		try {
			Connection connection = factory.newConnection();
			System.out.println("Connected: " + RabbitmqHost);
			Channel channel = connection.createChannel();

			
			String myQueue = "myMessagePropertiesQueue_"+ RECIPE_NR;
			boolean isDurable = false;
			boolean isExclusive = false;
			boolean isAutoDelete = false;

			channel.queueDeclare(myQueue, isDurable, isExclusive, isAutoDelete,null);

			ActualConsumer consumer = new ActualConsumer(channel);
			boolean _autoAck = true; 
			String consumerTag=  channel.basicConsume(myQueue, _autoAck, consumer);

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
