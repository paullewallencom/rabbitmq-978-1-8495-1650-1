package rmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ActualConsumer extends DefaultConsumer {
	public ActualConsumer(Channel channel) {
		super(channel);
	}
	
	 public void handleDelivery(String consumerTag,Envelope envelope, 
			 BasicProperties properties,byte[] body) throws java.io.IOException {
		 System.out.println("Message Received!");
	     String JSONString = new String(body);
		 Book myBook = Book.loadFromJSON(JSONString);
         System.out.println("Book id:" + myBook.getBookID());
         System.out.println("Book Description:" + myBook.getBookDescription());
         System.out.println("Book Author:" + myBook.getAuthor());
         System.out.println("Done!");
	}
}
