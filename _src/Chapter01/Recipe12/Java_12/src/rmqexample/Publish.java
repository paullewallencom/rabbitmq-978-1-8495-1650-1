package rmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 *
 */
import com.rabbitmq.client.MessageProperties;

public class Publish {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="12"; 
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Ensuring message transactionality **");
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
			
			String myQueue = "myTransactionalityQueue_"+RECIPE_NR;
			boolean isDurable = true; // n.b.
			boolean isExclusive = false;
			boolean isAutoDelete = false;

			channel.queueDeclare(myQueue, isDurable, isExclusive, isAutoDelete,null);
			
			channel.txSelect();
			for (int i = 1; i < 21; i++) {
				String message = "Transactionality message:" + i;
				channel.basicPublish("",myQueue, MessageProperties.PERSISTENT_TEXT_PLAIN ,message.getBytes());
				System.out.println("Transactionality message: " + message + " sent");
				channel.txCommit(); // the message is stored to the queue
				System.out.println("Commit done!");
			}
			
			channel.close();
			System.out.println("Finished.");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
