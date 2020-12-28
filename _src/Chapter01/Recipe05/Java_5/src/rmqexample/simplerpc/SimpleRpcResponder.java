package rmqexample.simplerpc;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Recipe number 7. Using RPC with messaging
 * */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.StringRpcServer;

public class SimpleRpcResponder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="5";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Using RPC with messaging (StringRpc Server Java) **");
		String RabbitmqHost = "localhost";
		if (args.length > 0)
			RabbitmqHost = args[0];

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
		try {
			Connection connection = factory.newConnection();
			System.out.println("Conneted:" + RabbitmqHost);
			Channel channel = connection.createChannel();
			String RpcQueueName="MySimpleRpcQueue_"+RECIPE_NR;
			channel.queueDeclare(RpcQueueName, false, false, false, null);
			System.out.println("Server RPC Ready on:" + RpcQueueName);
			
			StringRpcServer server = new StringRpcServer(channel, RpcQueueName) {
				public String handleStringCall(String request) {
					System.out.println("Java-Server Received request: " + request);
					return "Response form String RpcServer, form:" + request;
				}
			};
			server.mainloop();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
