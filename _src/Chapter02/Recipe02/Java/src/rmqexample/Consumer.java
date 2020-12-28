package rmqexample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {
	public static void main(String[] args) {
		System.out.println(Constants.HEADER);
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
			// mandatory here: we want to bind the queue to the exchange. But if we are
			// started before the producer, the exchange still doesn't exists and the 
			// binding will fail.
			channel.exchangeDeclare(Constants.exchange, "direct", false);
			Map<String, Object> arguments = new HashMap<String, Object>();
			// set per-message queue-timeout to 10 seconds
			arguments.put("x-message-ttl", 10000);
			channel.queueDeclare(Constants.queue, false, false, false, arguments);
			channel.queueBind(Constants.queue, Constants.exchange, Constants.routingKey);

			ActualConsumer consumer = new ActualConsumer(channel);
			String consumerTag = channel.basicConsume(Constants.queue, false, consumer);
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
