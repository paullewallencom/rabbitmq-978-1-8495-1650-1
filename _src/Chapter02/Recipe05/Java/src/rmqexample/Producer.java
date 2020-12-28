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
			Map<String, Object> arguments = new HashMap<String, Object>();
			arguments.put("alternate-exchange",Constants.alternateExchange);
			channel.exchangeDeclare(Constants.exchange, "direct", false, false, arguments);
			channel.exchangeDeclare(Constants.alternateExchange, "direct", false);
			/* we want to keep alerts => durable=true */
			channel.queueDeclare(Constants.missingAlertQueue, true, false, false, null);
			channel.queueBind(Constants.missingAlertQueue, Constants.alternateExchange, Constants.alertRK);

			Stats stats = new Stats();
			JSONWriter rabbitmqJson = new JSONWriter();			
			int msgCount=0;
			for(;;) {
				stats.Update();
				String statMsg = rabbitmqJson.write(stats);
				// just select the routing key randomly for the recipe. In a real application
				// the selection will be driven by the memory running low
				String routingKey = Math.random() < 0.8 ? Constants.infoRK : Constants.alertRK;
				channel.basicPublish(Constants.exchange, routingKey, null, statMsg.getBytes());
				++msgCount;
				System.out.println(routingKey + "   " + stats.toString());
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
