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
			channel.exchangeDeclare(Constants.exchange, "topic", false);
			channel.exchangeBind(Constants.exchange, Constants.rif_exchange_c1_8,Constants.rountingKey1_8);
			channel.exchangeBind(Constants.exchange, Constants.rif_exchange_c1_6,"#");

			String myqueue= channel.queueDeclare().getQueue();			
			channel.queueBind(myqueue, Constants.exchange, "#");
			
			/* */
			TraceConsumer consumer = new TraceConsumer(channel);
			String consumerTag = channel.basicConsume(myqueue, false, consumer);
			
			System.out.println("press any key to terminate");
			System.in.read();
			channel.exchangeUnbind(Constants.exchange, Constants.rif_exchange_c1_8,Constants.rountingKey1_8);
			channel.exchangeUnbind(Constants.exchange, Constants.rif_exchange_c1_6,"#");

			
			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
			System.out.println("DONE!" );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
