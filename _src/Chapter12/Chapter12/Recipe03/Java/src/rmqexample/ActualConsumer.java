package rmqexample;

import java.util.ArrayList;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.LongString;

public class ActualConsumer extends DefaultConsumer {
	private int msgCount;
	
	public ActualConsumer(Channel channel) {
		super(channel);
		msgCount=0;
	}

	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws java.io.IOException {
		String routingKey = envelope.getRoutingKey();
		String message = new String(body);
		
		Map<String,Object> headers = properties.getHeaders();
		LongString exchange_name = (LongString) headers.get("exchange_name");
		LongString node = (LongString) headers.get("node");
		
		System.out.println("------------------------------------------------------------");
		System.out.println("traced routing key: '" + routingKey +"'");
		System.out.println("exchange name:      '" + exchange_name.toString() + "'");
		System.out.println("node:               '" + node.toString() + "'");
		System.out.println("Message:            '" + message + "'");		
		System.out.println("------------------------------------------------------------");
		++msgCount;
		getChannel().basicAck(envelope.getDeliveryTag(), false);
	}

	public int getMsgCount() {
		return msgCount;
	}
}
