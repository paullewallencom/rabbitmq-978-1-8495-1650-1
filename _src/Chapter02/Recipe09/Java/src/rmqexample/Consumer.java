package rmqexample;

import java.io.IOException;

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
			/* preparing the queue and the exchange*/
			channel.exchangeDeclare(Constants.exchange, "direct", false);
			
			String myqueue= channel.queueDeclare().getQueue();			
			channel.queueBind(myqueue, Constants.exchange, Constants.alert_routing_key);
			
			String myqueueCC_BK= channel.queueDeclare().getQueue();			
			channel.queueBind(myqueueCC_BK, Constants.exchange, Constants.backup_alert_routing_key);
			
			String myqueueBCC_SA= channel.queueDeclare().getQueue();			
			channel.queueBind(myqueueBCC_SA, Constants.exchange, Constants.send_alert_routing_key);
			
			/* */
			StatsConsumer consumer = new StatsConsumer(channel);
			String consumerTag = channel.basicConsume(myqueue, false, consumer);

			CCStatsConsumer ccconsumer = new CCStatsConsumer(channel);
			String consumerTagCC_BK = channel.basicConsume(myqueueCC_BK, false, ccconsumer);
			
			BCCStatsConsumer bccconsumer = new BCCStatsConsumer(channel);
			String consumerTagBCC_SA = channel.basicConsume(myqueueBCC_SA, false, bccconsumer);
			
			System.out.println("press any key to terminate");
			System.in.read();
			channel.basicCancel(consumerTag);
			channel.basicCancel(consumerTagCC_BK);
			channel.basicCancel(consumerTagBCC_SA);
			channel.close();
			connection.close();
			System.out.println("DONE!" );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
