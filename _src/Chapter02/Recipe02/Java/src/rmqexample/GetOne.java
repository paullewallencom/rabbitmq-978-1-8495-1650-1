package rmqexample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

public class GetOne {
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
			channel.exchangeDeclare(Constants.exchange, "direct", false);
			Map<String, Object> arguments = new HashMap<String, Object>();
			// set per-message queue-timeout to 10 seconds
			arguments.put("x-message-ttl", 10000);
			channel.queueDeclare(Constants.queue, false, false, false, arguments);
			channel.queueBind(Constants.queue, Constants.exchange, Constants.routingKey);
			// get just one message.
			GetResponse response = channel.basicGet(Constants.queue, true );
			if (response != null) {
				String message = new String(response.getBody());
				Stats stats = Stats.loadFromJSON(message);
				System.out.println("Got one message:");
				System.out.println(stats.toString());
				System.out.println("DONE. Still in queue: about " + response.getMessageCount() + " messages");
			} else {
				System.out.println("DONE. No messages in queue");
			}
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
