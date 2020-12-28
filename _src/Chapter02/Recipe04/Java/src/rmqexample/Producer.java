package rmqexample;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Chapter 02 Recipe 01. How to let messages expire on the producer side.
 * */

import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.json.JSONWriter;

public class Producer {

	/**
	 * @param args
	 *            [0] RabbitmqHost
	 */
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
			/* preparing the queue and the exchange*/
			channel.exchangeDeclare(Constants.exchange, "direct", false);
			channel.exchangeDeclare(Constants.exchange_dead_letter, "direct", false);
			Map<String, Object> arguments = new HashMap<String, Object>();
			arguments.put("x-dead-letter-exchange",Constants.exchange_dead_letter);
			arguments.put("x-message-ttl", 10000);
			channel.queueDeclare(Constants.queue, false, false, false, arguments);
			channel.queueBind(Constants.queue, Constants.exchange, "");
			/* */
			
			
			Stats stats = new Stats();
			JSONWriter rabbitmqJson = new JSONWriter();
						
			int msgCount=0;
			for(;;) {
				stats.Update();
				String statMsg = rabbitmqJson.write(stats);
				System.out.println(stats.toString());				
				channel.basicPublish(Constants.exchange, "", null, statMsg.getBytes());
				++msgCount;
				if (System.in.available() > 0) break;
				Thread.sleep(1000);
			}
			channel.close();
			System.out.println("Done: " + msgCount + " messages sent");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
