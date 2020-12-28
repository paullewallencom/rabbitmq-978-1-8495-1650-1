package rmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="10 .6";
		System.out.println(" ** RabbitmqCookBook -  Recipe number "+RECIPE_NR+". Stress test producer **");
		String hostname = "localhost";
		if (args.length > 0) {
			hostname = args[0];
		}
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostname);
	

		try {
			Connection connection = factory.newConnection();


			System.out.println("Connected: " + hostname);
			Channel channel = connection.createChannel();
			
			String myExchengeTest="myExchangeTest";

			channel.exchangeDeclare(myExchengeTest, "fanout"); 
			String message;

			for (int i = 0; i < 2000000; i++) {
				message = "message number:"+i;
				channel.basicPublish(myExchengeTest,"", null ,message.getBytes());
				if ((i % 1000)==0){
					System.out.println(" reconnection ");

					channel.close();
					connection.close();
					connection = factory.newConnection();
					channel = connection.createChannel();
					
					System.out.println(" reconnected ");

					
				}
				
			}

		
			
			channel.close();
			System.out.println("Finished.");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	}


