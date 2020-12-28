package rmqexample;

import java.util.Iterator;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class StatsConsumer extends DefaultConsumer {
	
	public StatsConsumer(Channel channel) {
		super(channel);

	}

	 public void handleDelivery(
			 String consumerTag, 
			 Envelope envelope, 
			 BasicProperties properties, 
			 byte[] body) throws java.io.IOException {
     String message = new String(body);
     Stats stats = Stats.loadFromJSON(message);
     System.out.println(stats.toString());
     
     
     if (properties.getHeaders()!=null){
			System.out.println("message header:");
			Iterator<String> iterator = properties.getHeaders().keySet().iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					System.out.println(key + " - " + properties.getHeaders().get(key));
				}
  
	
	}
     System.out.println("Consumer:" + this.getClass().getName()+  " Message routing key: "+envelope.getRoutingKey());
     
     getChannel().basicAck(envelope.getDeliveryTag(), false);
        
     
	 }

}
