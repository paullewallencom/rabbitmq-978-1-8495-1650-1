package rmqexample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {
	public static void main(String[] args) {
		String RECIPE_NR="10 .6";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Stress test Consumer**");
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
			String myExchengeTest="myExchangeTest";

			channel.exchangeDeclare(myExchengeTest, "fanout"); 
			String myQueue = channel.queueDeclare().getQueue();
			
			channel.queueBind(myQueue, myExchengeTest, "");

			ActualConsumer consumer = new ActualConsumer(channel);
			String consumerTag = channel.basicConsume(myQueue, false, consumer);
			System.out.println("press any key to terminate");
			System.in.read();
			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
			System.out.println("DONE. Received " + Integer.toString(consumer.getMsgCount()) + " messages" );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
