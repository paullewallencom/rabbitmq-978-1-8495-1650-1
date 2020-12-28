package rmqexample.direct;

import java.util.StringTokenizer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="7";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+	 RECIPE_NR+". Working with message routing (client Java Direct-Consumer) **");
		String RabbitmqHost = "localhost";
		String keys = "";
		if (args.length > 0)
			RabbitmqHost = args[0];
		
		// in order to use this you have to set also the first parameter 
		// to the list of semicolon separated keys 
		// for example:
		// java Consumer localhost sport;technology
		if (args.length > 1) keys = args[1]; 
		
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
		try {
			Connection connection = factory.newConnection();
			System.out.println("Conneted: " + RabbitmqHost);
			Channel channel = connection.createChannel();
			String myExchange = "myDirectExchangeRoutingKey_"+ RECIPE_NR;
			channel.exchangeDeclare(myExchange, "direct", false, false, null);
			String myQueue = channel.queueDeclare().getQueue();
			
			StringTokenizer rkeyTokenizer = new StringTokenizer(keys,";");
			while (rkeyTokenizer.hasMoreElements()){
				String bindingKey = rkeyTokenizer.nextToken();
				/// multi bind with different routing key
				channel.queueBind(myQueue,myExchange,bindingKey);
				System.out.println("Consumer bound with key: " + bindingKey);
			}
			ActualConsumer consumer = new ActualConsumer(channel);
			String consumerTag = channel.basicConsume(myQueue, true, consumer);
			System.out.println("Consumer Ready. Bound keys:" + keys +"   - press a key to terminate");
			System.in.read();
			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
