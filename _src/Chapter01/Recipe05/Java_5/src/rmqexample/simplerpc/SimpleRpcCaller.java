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
import com.rabbitmq.client.RpcClient;

public class SimpleRpcCaller {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="5";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Using RPC with messaging (StringRpcClient Client Java) **");
		String RabbitmqHost = "localhost";
		if (args.length > 0)
			RabbitmqHost = args[0];
		
		String rpcParam="My Default RPC Param";
		if (args.length > 1)
			rpcParam = args[1];
		

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
		try {
			Connection connection = factory.newConnection();
			System.out.println("Conneted:"+RabbitmqHost);
			Channel channel = connection.createChannel();
			String RpcQueueName="MySimpleRpcQueue_"+RECIPE_NR;
			RpcClient rcpClient = new RpcClient(channel, "", RpcQueueName);
			String callResponse = rcpClient.stringCall(rpcParam);
			System.out.println("Java-Received:"+callResponse);
			channel.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
