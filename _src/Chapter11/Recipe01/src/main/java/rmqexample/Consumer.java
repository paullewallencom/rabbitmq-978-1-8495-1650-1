package rmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {
	public static void main(String[] args) {
		System.out.println(Constants.HEADER);
		String RabbitmqUri = "amqp://guest:guest@localhost";
		if (args.length > 0)
			RabbitmqUri = args[0];

		ConnectionFactory factory = new ConnectionFactory();
		try 
		{
			factory.setUri(RabbitmqUri);
			
			Connection connection = factory.newConnection();
			System.out.println("Connected: " + RabbitmqUri);
			Channel channel = connection.createChannel();
			ActualConsumer consumer = new ActualConsumer(channel);
			String myQueue = Constants.queue;
			String consumerTag = channel.basicConsume(myQueue , false, consumer);
			System.out.println("press any key to terminate");
			System.in.read();
			channel.basicCancel(consumerTag);
			Thread.sleep(1000);
			channel.close();
			connection.close();
			System.out.println("DONE" );
		}
		 catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
