package rmqexample;
/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Recipe number 5. broadcasting messages 
 * */
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="6";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Broadcast messages (java consumer) **");
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
			String myExchange = "myLastnews.fanout_"+RECIPE_NR;
			channel.exchangeDeclare(myExchange, "fanout");

			// autocreate queue name 
			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind(queueName, myExchange, "");
			ActualConsumer consumer = new ActualConsumer(channel);

			boolean autoAck = true; 
			channel.basicConsume(queueName, autoAck, consumer);
			System.out.println("Consuming messages - press any key to terminate");
			System.in.read();
			channel.close();
			connection.close();
			System.out.println("Consumer exited");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
