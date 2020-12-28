package rmqexample.rpc;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.tools.json.JSONReader;
import com.rabbitmq.tools.json.JSONWriter;

public class RpcResponderConsumer extends DefaultConsumer {
	BookStore bookstore;
	JSONReader jsonReader;
	JSONWriter jsonWriter;

	public RpcResponderConsumer(Channel channel) {
		super(channel);
		jsonReader = new JSONReader();
		jsonWriter = new JSONWriter();
		bookstore = new BookStore();
	}

	public void handleDelivery(String consumerTag, 
			Envelope envelope,
			AMQP.BasicProperties properties, 
			byte[] body) throws java.io.IOException {
		
		String reply = null;
		try {
			String message = new String(body);
			Integer idToFind =(Integer)jsonReader.read(message);
			System.out.println("Searching for: " + idToFind); 
			Book book = bookstore.GetBook(idToFind);
			
			reply = book != null ? jsonWriter.write(book) : "ERROR: BOOK not found";
			System.out.println("JSON response: " + reply);
		} catch (Exception e) {
			reply ="ERROR: internal server error";
			System.err.println(e);
		} finally {
			BasicProperties replyProperties = new BasicProperties.Builder().correlationId(properties.getCorrelationId()).build();
		  // you have to send the reply on the client
		  // the client is blocked until this publish.
		  // In rpc mode it's important send always a reply.
		  getChannel().basicPublish("", properties.getReplyTo(), replyProperties, reply.getBytes());
		  // The message must be removed form the queue.
		  // Anyway if the sever can't remove the message the client is safe 
		  // with the correlation id attribute.
		  getChannel().basicAck(envelope.getDeliveryTag(), false);
		}
	}
}
