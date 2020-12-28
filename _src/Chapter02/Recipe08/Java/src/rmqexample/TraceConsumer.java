package rmqexample;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class TraceConsumer extends DefaultConsumer {
	
	public TraceConsumer(Channel channel) {
		super(channel);

	}

	 public void handleDelivery(
			 String consumerTag, 
			 Envelope envelope, 
			 BasicProperties properties, 
			 byte[] body) throws java.io.IOException {
		     String message = new String(body);
		     System.out.println(message);
        
     
	 }

}
