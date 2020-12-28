package rmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Constants.HEADER);
		String RabbitmqUri = "amqp://guest:guest@localhost";
		if (args.length > 0)
			RabbitmqUri = args[0];

		// Check your cloudamq uri connection.
		ConnectionFactory factory = new ConnectionFactory();
		try {

			factory.setUri(RabbitmqUri);
			Connection connection = factory.newConnection();
			System.out.println("Connected: " + RabbitmqUri);
			Channel channel = connection.createChannel();

		
			channel.queueDeclare(Constants.queue,true,false,false,null);
			System.out.println("Message publishing started - Press enter to terminate");
			for (int i=1;;i++) {
				String jsonmessage = "test message "+ i;
				channel.basicPublish("",Constants.queue, null, jsonmessage.getBytes());
				System.out.println(" Message sent :" + jsonmessage);
				if (System.in.available()>0) {
					break;
				}
				Thread.sleep(1000);
			}
			channel.close();
			System.out.println("Message publishing completed.");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	}


