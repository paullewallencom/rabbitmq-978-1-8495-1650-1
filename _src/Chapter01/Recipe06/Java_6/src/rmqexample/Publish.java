package rmqexample;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Recipe number 5. broadcasting messages 
 * */
import java.util.Date;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.json.JSONWriter;

public class Publish {

	/**
	 * @param args
	 *            [0] RabbitmqHost
	 */
	public static void main(String[] args) {
		String RECIPE_NR="6";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Broadcasting messages **");
		String RabbitmqHost = "localhost";
		if (args.length > 0)
			RabbitmqHost = args[0];

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
	
		try {
			Connection connection = factory.newConnection();
			System.out.println("Connected: " + RabbitmqHost);
			Channel channel = connection.createChannel();

			String myExchange = "myLastnews.fanout_"+RECIPE_NR;
			channel.exchangeDeclare(myExchange, "fanout");
			System.out.println("Message publishing started - Press enter to terminate");
			for (int i=1;;i++) {
				LastNews lastnews = new LastNews();
				lastnews.setNewsText("New phoneX has arrived! (" + Integer.toString(i) +")");
				lastnews.setNewsKind("Technology");
				Date d = new Date();
				lastnews.setNewsDate(d.toString());
				
				JSONWriter rabbitmqJson = new JSONWriter();
				String jsonmessage = rabbitmqJson.write(lastnews);
				
				// send a broadcast message
				channel.basicPublish(myExchange, "", null, jsonmessage.getBytes());
				System.out.println(" Message sent :" + jsonmessage);
				if (System.in.available()>0) {
					break;
				}
				Thread.sleep(1000);
			}
			channel.close();
			System.out.println("Message publishing completed.");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
