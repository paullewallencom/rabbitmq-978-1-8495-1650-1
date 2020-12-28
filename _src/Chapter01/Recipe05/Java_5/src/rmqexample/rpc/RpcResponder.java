package rmqexample.rpc;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RpcResponder {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="5";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Using RPC with messaging (RpcCallBack Server Java) **");
		String rabbitmqHost = "localhost";
		if (args.length > 0)
			rabbitmqHost = args[0];

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rabbitmqHost);

		try {
			Connection connection = factory.newConnection();
			System.out.println("Conneted:" + rabbitmqHost);
			Channel channel = connection.createChannel();
			String requestQueue = "MyRpcQueue_"+RECIPE_NR;
			channel.queueDeclare(requestQueue, false, false, false, null);
			RpcResponderConsumer consumer = new RpcResponderConsumer(channel);
			String consumerTag = channel.basicConsume(requestQueue, false, consumer);
			System.out.println("Consuming messages from queue: " + requestQueue + " - press any key to exit");
			System.in.read();
			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
			System.out.println("Execution completed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
