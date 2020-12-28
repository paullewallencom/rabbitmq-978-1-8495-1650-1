package rmqexample.topic;

import java.util.StringTokenizer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="01/08"; 
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Working with message routing (client Java Topic-Consumer) **");
		String RabbitmqHost = "localhost";
		String RountingKey = "technology.#;*.golf.#";
		if (args.length > 0)
			RabbitmqHost = args[0];
		
		if (args.length > 1)
			RountingKey = args[1]; // in order to use this you have to set also the first parameter 
			// for example java ....Consumer localhost myroutingkey;myroutingkey2;myrountingkey3.
		
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
		try {
			Connection connection = factory.newConnection();
			System.out.println("Conneted:" + RabbitmqHost);
			Channel channel = connection.createChannel();
			String myExchange = "myTopicExchangeRoutingKey_"+RECIPE_NR;
			
			channel.exchangeDeclare(myExchange, "topic", false, false, null);
			String myQueue = channel.queueDeclare().getQueue();
			
			/// you can combine the routingkey with:
			// # for example sport.# or #.ebook 
			// * for example sport.tennis.* or sport.*.ebook
			StringTokenizer  rkeyTokenizer = new StringTokenizer(RountingKey,";");
			while (rkeyTokenizer.hasMoreElements()){
				String bindingKey = rkeyTokenizer.nextToken();
				System.out.println("Consumer Bind with key:" + bindingKey);
				/// multi bind with different routing key
				 channel.queueBind(myQueue,myExchange,bindingKey);
			}
			ActualConsumer consumer = new ActualConsumer(channel);
			String consumerTag = channel.basicConsume(myQueue, true, consumer);
			System.out.println("Consumer Ready: key bind:" + RountingKey +"   - press a key to terminate");
			System.in.read();
			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
