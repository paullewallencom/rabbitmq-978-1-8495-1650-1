package rmqexample;
/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 *
 */

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.BasicProperties;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 *
 */

public class Publish {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="11"; 
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Using a basic properties message **");
		String hostname = "localhost";
		if (args.length > 0) {
			hostname = args[0];
		}
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostname);
		try {
			Connection connection = factory.newConnection();

			System.out.println("Connected: " + hostname);
			Channel channel = connection.createChannel();
			
			String myQueue = "myMessagePropertiesQueue_"+RECIPE_NR;
			boolean isDurable = false;
			boolean isExclusive = false;
			boolean isAutoDelete = false;

			channel.queueDeclare(myQueue, isDurable, isExclusive, isAutoDelete,null);
			
			Map<String,Object> headerMap = new HashMap<String, Object>();
			headerMap.put("key1", "value1");
			headerMap.put("key2", new Integer(50) );
			headerMap.put("key3", new Boolean(false));
			headerMap.put("key4", "value4");
			
			BasicProperties messageProperties  = new BasicProperties.Builder()
			.timestamp(new Date())
			.userId("guest")
			.appId("app id: 20")
			.deliveryMode(1)
			.priority(1)
			.headers(headerMap)
			.build();
			
			
			String message = "This is a message with BasicProperties";
			channel.basicPublish("",myQueue, messageProperties ,message.getBytes());
			System.out.println("Message with BasicProperties sent: " + message);
			
			channel.close();
			System.out.println("Finished.");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	

}
