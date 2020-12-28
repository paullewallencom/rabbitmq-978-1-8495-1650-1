package rmqexample.topic;

import java.util.Date;

import rmqexample.Book;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ActualConsumer extends DefaultConsumer {
	public ActualConsumer(Channel channel) {
		super(channel);
	}
	
	 
	
	public void handleDelivery(
			 String consumerTag, 
			 Envelope envelope, 
			 BasicProperties properties, 
			 byte[] body) throws java.io.IOException {
     String message = new String(body);
     System.out.println("Received: " + message);
 	 System.out.println("Java-Received rounting key:" + envelope.getRoutingKey());
	 Book book = Book.loadFromJSON(message);
	 System.out.println("Book id:" + book.getBookID());
	 System.out.println("Book Description:" + book.getBookDescription());
	 System.out.println("Book Author:" + book.getAuthor());
	 System.out.println("Book Kind:" + book.getBookKind());
	 System.out.println("Book Format:" + book.getFormat());
	 System.out.println("Book Argoument:" + book.getArgoument());
	 System.out.println("***************************");
	 System.out.println(" Last message received:" + new Date().toString() );
	}
}
