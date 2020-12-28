package rmqexample;

import java.io.IOException;
import java.util.Random;


import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

	public static void main(String[] args) {
		for (int i = 0; i < 1000; i++) {



			try {
				System.out.println("Hello World!");
				ConnectionFactory factory = new ConnectionFactory();
				Connection connection;
				String rabbitmqhost1="localhost";
				String rabbitmqhost2="localhost";
				if (args.length>=2){
					rabbitmqhost1=args[0];
					rabbitmqhost2=args[1];
				}
				Random randomGenerator = new Random();
				int randomInt = randomGenerator.nextInt(100);
				System.out.println("random:" + randomInt);
				Address[] addrArr = new Address[2];
				if (randomInt %2==0) {
					addrArr[0] =  new Address(rabbitmqhost1, 5672);
					addrArr[1] = new Address(rabbitmqhost2, 5672);
				} else {

					addrArr[0] =  new Address(rabbitmqhost2, 5672);
					addrArr[1] = new Address(rabbitmqhost1, 5672);	
				}




				connection = factory.newConnection(addrArr);

				try{
					Channel channel;
					channel = connection.createChannel();

					String message = "Hello World!";
					channel.basicPublish("amq.fanout", "", null, message.getBytes());
					System.out.println(" [x] Sent '" + message + "'");


					channel.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}


				Thread.sleep(5000);

				connection.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
