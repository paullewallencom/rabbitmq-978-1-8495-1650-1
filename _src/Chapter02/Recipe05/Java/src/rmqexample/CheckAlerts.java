package rmqexample;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

public class CheckAlerts {
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
			channel.queueDeclare(Constants.missingAlertQueue, true, false, false, null);
			// get just one message, autoAck = false and the ack won't be sent: 
			// we are not consuming any message
			GetResponse response = channel.basicGet(Constants.missingAlertQueue, false );
			if (response != null) {
				String message = new String(response.getBody());
				Stats stats = Stats.loadFromJSON(message);
				int alertCount = response.getMessageCount() + 1;
				System.out.printf("WARNING: There are %d missing alerts. The first one:\n", alertCount);
				System.out.println(stats.toString());
			} else {
				System.out.println("No missing alerts.");
			}
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
