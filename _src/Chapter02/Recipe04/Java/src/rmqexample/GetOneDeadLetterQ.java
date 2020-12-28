package rmqexample;

import java.io.IOException;
import java.util.Iterator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

public class GetOneDeadLetterQ {
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
			channel.exchangeDeclare(Constants.exchange_dead_letter, "direct", false);
			
			channel.queueDeclare(Constants.queue_dead_letter, false, false, false, null);
			channel.queueBind(Constants.queue_dead_letter, Constants.exchange_dead_letter, "");
			// get just one message.
			GetResponse response = channel.basicGet(Constants.queue_dead_letter, true );
			if (response != null) {
				String message = new String(response.getBody());
				Stats stats = Stats.loadFromJSON(message);
				System.out.println("Got one message:");
				System.out.println(stats.toString());
				if (response.getProps().getHeaders()!=null){
					System.out.println("Dead letter message header:");
					Iterator<String> iterator = response.getProps().getHeaders().keySet().iterator();
						while (iterator.hasNext()) {
							String key = (String) iterator.next();
							System.out.println(key + " - " + response.getProps().getHeaders().get(key));
						}
				
				
			}
			     
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
