package rmqexample;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Chapter 02 Recipe 06. 
 * */

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP.BasicProperties;
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
			channel.queueDeclare(Constants.queue, true, false, false, null);
			
			Order bookOrder = new Order();
			bookOrder.setUserID(123456);
			bookOrder.setBookID(56789);
			bookOrder.setAddress("10 East 12th Street, Neverland");
			JSONWriter rabbitmqJson = new JSONWriter();			
			String bookOrderMsg = rabbitmqJson.write(bookOrder);
			Map<String,Object> headerMap = new HashMap<String, Object>();
		    headerMap.put(Constants.senderip, InetAddress.getLocalHost().getHostAddress());
			BasicProperties messageProperties  = new BasicProperties.Builder()
			.timestamp(new Date())
			.userId("guest")
			.deliveryMode(2)
			.headers(headerMap)
			.build();
			 System.out.println("Sending order....");
			 System.out.println("UserID:" + bookOrder.getUserID());
			 System.out.println("BookID:" + bookOrder.getBookID());
			 System.out.println("Address:" + bookOrder.getAddress());
			channel.basicPublish("",Constants.queue, messageProperties, bookOrderMsg.getBytes());
			System.out.println("The order has been sent!");
					
			channel.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
