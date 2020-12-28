package rmqexample.rpc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.tools.json.JSONReader;

public class RpcCallerConsumer extends DefaultConsumer {
	JSONReader jsonReader;
	Map<String,String> actions;

	public RpcCallerConsumer(Channel channel) {
		super(channel);
		jsonReader = new JSONReader();
		actions = Collections.synchronizedMap(new HashMap<String, String>());
	}

	public void OnReply(String action, String response)
	{
		if (response.startsWith("ERROR:")) {
			System.out.println("ERROR recevied: " + response);
		} else {
			Book book = Book.loadFromJSON(response);
			System.out.println("Performing action on following book: '" + action + "'");
			System.out.println("  Book id:" + book.getBookID());
			System.out.println("  Book Description:" + book.getBookDescription());
			System.out.println("  Book Author:" + book.getAuthor());
		}
	}
	
	public void AddAction(String messageIdentifier, String action)
	{
		actions.put(messageIdentifier , action);
	}
	
	public void handleDelivery(String consumerTag, 
			Envelope envelope,
			AMQP.BasicProperties properties, 
			byte[] body) throws java.io.IOException {

		String messageIdentifier = properties.getCorrelationId();
		String action = actions.get(messageIdentifier);
		actions.remove(messageIdentifier);

		String response = new String(body);
		OnReply(action, response);
	}
}
