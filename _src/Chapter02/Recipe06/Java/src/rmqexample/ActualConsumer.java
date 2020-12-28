package rmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

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
     Order bookOrder = Order.loadFromJSON(message);
     System.out.println("Order data:");
     System.out.println("UserID:" + bookOrder.getUserID());
     System.out.println("BookID:" + bookOrder.getBookID());
     System.out.println("Address:" + bookOrder.getAddress());
     System.out.println("Header data:"); 
     System.out.println("The message has been placed by "+properties.getUserId());
     System.out.println("The message has been sent at "+properties.getTimestamp());   
     System.out.println("The source ip is: "+properties.getHeaders().get(Constants.senderip));
     
     
	}

}
