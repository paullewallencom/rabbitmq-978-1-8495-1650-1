package rmqexample;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Recipe number 1. Buffering data using messaging.
 * */


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Publish {

	/**
	 * @param args
	 *            [0] RabbitmqHost
	 */
	public static void main(String[] args) {
		String RECIPE_NR="03/01";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Using vhosts **");
		String hostname = "localhost";
		if (args.length > 0) {
			hostname = args[0];
		}
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostname);
		factory.setVirtualHost("book_orders");

		try {
			Connection connection = factory.newConnection();


			System.out.println("Connected: " + hostname);
			Channel channel = connection.createChannel();
			
			String myQueue="myFirstQueue";
			boolean isDurable = true;
			boolean isExclusive = false;
			boolean isAutoDelete = false;
			// let's create our first queue
			channel.queueDeclare(myQueue, isDurable, isExclusive, isAutoDelete, null);
			
			String message;

			message = "My NOT persistent message to myfirstqueue";
			channel.basicPublish("",myQueue, null ,message.getBytes());
			System.out.println("Message sent: " + message);

			message = "My persistent message to myfirstqueue";
			channel.basicPublish("",myQueue, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
			System.out.println("Message sent: " + message);
			
			channel.close();
			System.out.println("Finished.");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
