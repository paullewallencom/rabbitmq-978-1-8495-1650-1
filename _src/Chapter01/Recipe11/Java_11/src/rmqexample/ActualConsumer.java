package rmqexample;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;



public class ActualConsumer extends DefaultConsumer {
	public ActualConsumer(Channel channel) {
		super(channel);
	}
	
	 public void handleDelivery(String consumerTag,Envelope envelope, 
			 BasicProperties properties,byte[] body) throws java.io.IOException {
	System.out.println("*********** message header*********************");
	System.out.println("Message sent by sender at:" + properties.getTimestamp());
    System.out.println("Message sent by user:" + properties.getUserId());
    System.out.println("Message sent by App:" + properties.getAppId());
    
    System.out.println("all properties :" + properties.toString());
    
    System.out.println("*************message body ***********************");
    String message = new String(body);
    System.out.println("Message Body:"+message);
	
    
	
	}
}
