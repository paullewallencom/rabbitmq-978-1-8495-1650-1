package rmqexample.blocking;

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
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer {
	public static void main(String[] args) {
		System.out.println(" ** RabbitmqCookBook - Recipe number 3. Consuming messages **");
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
			
			// run the Recipe_1 example for publishing messages to the queue.
			String myQueue="myFirstQueue";
			boolean isDurable = true;
			boolean isExclusive = false;
			boolean isAutoDelete = false;
			channel.queueDeclare(myQueue, isDurable, isExclusive, isAutoDelete, null);
			
			QueueingConsumer consumer = new QueueingConsumer(channel);
			boolean autoAck = true; 
			channel.basicConsume(myQueue, autoAck, consumer);
			
			System.out.println("Consumer Ready");
			while (true) {
				Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());
				System.out.println("Received: " + message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
