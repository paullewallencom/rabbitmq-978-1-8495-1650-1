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
		String RECIPE_NR="10"; 
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR +". Distributing messages to many consumers (Url download) **");
		String RabbitmqHost = "localhost";
		if (args.length > 0)
			RabbitmqHost = args[0];
		

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
		try {
			Connection connection = factory.newConnection();
			System.out.println("Connected: " + RabbitmqHost);
			Channel channel = connection.createChannel();

			
			String myQueue = "myUrlDownloadQueue_"+RECIPE_NR;
			boolean isDurable = false;
			boolean isExclusive = false;
			boolean isAutoDelete = false;

			channel.queueDeclare(myQueue, isDurable, isExclusive, isAutoDelete,null);
			//int prefetchCount = 1;
			//channel.basicQos(prefetchCount);

			ActualConsumer consumer = new ActualConsumer(channel);
			boolean autoAck = false; // <--- the message needs an explicit ACK
					
			String consumerTag = channel.basicConsume(myQueue, autoAck, consumer);
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
