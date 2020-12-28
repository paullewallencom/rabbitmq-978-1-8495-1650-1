package rmqexample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Consumer {
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
			Map<String, Object> arguments = new HashMap<String, Object>();
			arguments.put("alternate-exchange",Constants.alternateExchange);
			channel.exchangeDeclare(Constants.exchange, "direct", false, false, arguments);
			// create a private queue and bind it to the statistics exchange.
			// consume from it with autoAck = true
			String queue = channel.queueDeclare().getQueue();
			channel.queueBind(queue, Constants.exchange, Constants.infoRK);
			channel.queueBind(queue, Constants.exchange, Constants.alertRK);
			ActualConsumer consumer = new ActualConsumer(channel);
			String consumerTag = channel.basicConsume(queue, true, consumer);

			// a 2nd consumer, to check for missing alerts too
			ActualConsumer missingAlertConsumer = new ActualConsumer(channel);
			String missingAlertConsumerTag = channel.basicConsume(Constants.missingAlertQueue, true, missingAlertConsumer);
			
			System.out.println("press any key to terminate");
			System.in.read();
			channel.basicCancel(missingAlertConsumerTag);
			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
			System.out.printf("DONE. Recovered %d missing alerts. Received %d messages", 
					missingAlertConsumer.getMsgCount(), consumer.getMsgCount() );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
