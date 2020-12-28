package rmqexample;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Chapter 02 Recipe 01. How to let messages expire on the producer side.
 * */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.json.JSONWriter;
import rmqexample.Constants;

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
			channel.exchangeDeclare(Constants.exchange, "direct", false);
			channel.queueDeclare(Constants.queue, false, false, false, null);
			channel.queueBind(Constants.queue, Constants.exchange, Constants.routingKey);
			Stats stats = new Stats();
			JSONWriter rabbitmqJson = new JSONWriter();
			int msgCount=0;
			for(;;) {
				stats.Update();
				String statMsg = rabbitmqJson.write(stats);
				System.out.println(stats.toString());				
				channel.basicPublish(Constants.exchange, Constants.routingKey, null, statMsg.getBytes());
				++msgCount;
				if (System.in.available() > 0) break;
				Thread.sleep(1000);
			}
			channel.queueDelete(Constants.queue);
			channel.close();
			System.out.println("Done: " + msgCount + " messages sent, queue \"" + Constants.queue + "\" deleted");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
