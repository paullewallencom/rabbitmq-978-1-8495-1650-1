package rmqexample;

import java.io.IOException;
import java.util.Random;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ReliableClient {

	protected Random rnd;
	protected Connection connection;
	protected Channel channel;

	public ReliableClient() {
		rnd = new Random();
	}

	protected void waitForConnection() throws InterruptedException {
		while (true) {
			ConnectionFactory factory = new ConnectionFactory();
			int delta = rnd.nextInt(Constants.hosts.length);
			Address[] addrArr = new Address[Constants.hosts.length];
			for (int i = 0; i < Constants.hosts.length; ++i) {
				// let the hosts be tried in random order.
				int j = (i + delta) % Constants.hosts.length;
				addrArr[i] = new Address(Constants.hosts[j], Constants.port);
			}
	
			try {
				connection = factory.newConnection(addrArr);
				channel = connection.createChannel();
				channel.exchangeDeclare(Constants.exchange, "direct", false);
				channel.queueDeclare(Constants.queue, Constants.durableQueue, Constants.exclusiveQueue, Constants.autodeleteQueue, null);
				channel.queueBind(Constants.queue, Constants.exchange,
						Constants.routingKey);
	
				return;
			} catch (Exception e) {
				e.printStackTrace();
				// ignore errors. In a production case, it is important to
				// handle different kind of errors and give the application
				// some hint on what to do in case it is not possible to
				// connect after some timeouts, properly notifying persistent
				// errors
				disconnect();
				Thread.sleep(1000);
			}
		}
	}

	protected void disconnect() {
		try {
			if (channel != null && channel.isOpen()) {
				channel.close();
				channel = null;
			}
			
			if (connection != null && connection.isOpen()) {
				connection.close();
				connection = null;
			}
		} catch (IOException e) {
			// just ignore 
			e.printStackTrace();
		}
	}

}