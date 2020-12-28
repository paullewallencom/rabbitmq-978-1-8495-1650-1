package rmqexample;

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
     // RabbitMQ JSON deserializer currently does not perform reflection.
     // see Google GSON and other more complete solutions

     LastNews lastNews = LastNews.loadFromJSON(message);
     System.out.println("Java-Received");
     System.out.println("LastNews Date:"+ lastNews.getNewsDate());
     System.out.println("LastNews Kind:"+ lastNews.getNewsKind());
     System.out.println("LastNews Text:"+ lastNews.getNewsText());
	}
}
