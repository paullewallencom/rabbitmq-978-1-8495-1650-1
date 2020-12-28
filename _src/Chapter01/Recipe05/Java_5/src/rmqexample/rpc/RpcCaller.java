package rmqexample.rpc;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE] 
 * 
 */

import java.util.Random;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.json.JSONWriter;

public class RpcCaller {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR = "5";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+5+". Using RPC with messaging (RpcCallBack client Java) **");
		String rabbitmqHost = "localhost";
		if (args.length > 0)
			rabbitmqHost = args[0];

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rabbitmqHost);
		Connection connection = null;
		Channel channel = null;
		try {
			connection = factory.newConnection();
			System.out.println("Conneted:" + rabbitmqHost);
			channel = connection.createChannel();
			String requestQueue = "MyRpcQueue_"+RECIPE_NR;
			channel.queueDeclare(requestQueue, false, false, false, null);
			String replyQueue = channel.queueDeclare().getQueue();
			RpcCallerConsumer consumer = new RpcCallerConsumer(channel);
			// start waiting for replies from the private queue on a consumer thread
			channel.basicConsume(replyQueue, true, consumer);


			JSONWriter jsonwriter = new JSONWriter();
			
			Random rnd = new Random();
			System.out.println("Starting RPC request loop - press enter to exit");
			for(int i=0; /* forever */ ; ++i) {
				int idToBeFound = rnd.nextInt(10);
				String messageRequest = jsonwriter.write(new Integer(idToBeFound));
				String messageIdentifier = java.util.UUID.randomUUID().toString();
				consumer.AddAction(messageIdentifier, "perform action X on request n. " + Integer.toString(i));
				BasicProperties props = new BasicProperties.Builder()
						.correlationId(messageIdentifier)
						.replyTo(replyQueue).build();
				channel.basicPublish("", requestQueue, props,messageRequest.getBytes());
				System.out.println("Search request for id: " + idToBeFound + " sent! Ready for the next already...");
				if (System.in.available() > 0) break;
				Thread.sleep(1000);
			}
			channel.close();
			connection.close();
			System.out.println("Execution completed.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
