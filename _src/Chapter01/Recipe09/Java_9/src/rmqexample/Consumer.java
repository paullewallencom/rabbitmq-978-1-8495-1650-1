package rmqexample;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * 
 * Recipe number 3. Consuming messages.
 * */

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {

	/**
	 * @param args
	 * In order to use this recipe you can send a message with the recipe number two. 
	 * the example use the same queue (myfirstqueue)
	 */
	public static void main(String[] args) {
		System.out.println(" ** RabbitmqCookBook - Recipe number 9. Guaranteeing messages processing **");
		String RabbitmqHost = "localhost";
		if (args.length > 0)
			RabbitmqHost = args[0];
		

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
		try {
			Connection connection = factory.newConnection();
			System.out.println("Connected: " + RabbitmqHost);
			Channel channel = connection.createChannel();

			// / check out the Recipe 2 for publish message to the queue.
			String myQueue = "myFirstQueue";
			boolean isDurable = true;
			boolean isExclusive = false;
			boolean isAutoDelete = false;

			channel.queueDeclare(myQueue, isDurable, isExclusive, isAutoDelete,null);

			ActualConsumer consumer = new ActualConsumer(channel);
			
			boolean _autoAck = false; // <--- the message needs an explicit ACK
			String consumerTag = channel.basicConsume(myQueue, _autoAck, consumer);
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
