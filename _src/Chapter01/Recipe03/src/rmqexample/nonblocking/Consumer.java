package rmqexample.nonblocking;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {
	public static void main(String[] args) {
		System.out.println(" ** RabbitmqCookBook - Recipe number 3. Consuming messages + non-blocking semantics **");
		String RabbitmqHost = "localhost";
		if (args.length > 0)
			RabbitmqHost = args[0];
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
		try 
		{
			// optional: if not defined RabbitMQ will define and handle its own thread pool
			ExecutorService eService = Executors.newFixedThreadPool(10);
			Connection connection = factory.newConnection(eService);
			System.out.println("Connected: " + RabbitmqHost);
			Channel channel = connection.createChannel();
			
			/// check out the Recipe 1 for publish message to the queue.
			String myQueue="myFirstQueue";
			boolean isDurable = true;
			boolean isExclusive = false;
			boolean isAutoDelete = false;
			channel.queueDeclare(myQueue, isDurable, isExclusive, isAutoDelete, null);

			ActualConsumer consumer = new ActualConsumer(channel);
			boolean autoAck = true; 
			String consumerTag = channel.basicConsume(myQueue, autoAck, consumer);
			System.out.println("Consumer Ready - press a key to terminate");
			System.in.read();
			
			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
			eService.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
