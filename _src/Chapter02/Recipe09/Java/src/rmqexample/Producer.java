package rmqexample;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Chapter 02 Recipe 01. How to let messages expire on the producer side.
 * */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.json.JSONWriter;
import com.rabbitmq.client.AMQP.BasicProperties;

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
			Stats stats = new Stats();
			JSONWriter rabbitmqJson = new JSONWriter();
						
			int msgCount=0;
			for(;;) {
				stats.Update();
				String statMsg = rabbitmqJson.write(stats);
				System.out.println(stats.toString());				
				
				Map<String, Object> headerMap = new HashMap<String, Object>();
				List<String> ccList = new ArrayList<String>();
				ccList.add(Constants.backup_alert_routing_key);
				headerMap.put("CC", ccList);
				List<String> bccList = new ArrayList<String>();
				bccList.add(Constants.send_alert_routing_key);
				headerMap.put("BCC", bccList);
				BasicProperties messageProperties= new BasicProperties.Builder()
				.headers(headerMap)
				.build();
				channel.basicPublish(Constants.exchange, Constants.alert_routing_key, messageProperties, statMsg.getBytes());
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
