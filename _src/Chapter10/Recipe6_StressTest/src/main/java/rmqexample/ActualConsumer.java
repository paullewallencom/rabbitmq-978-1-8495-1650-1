package rmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ActualConsumer extends DefaultConsumer {
	private int msgCount;

	public ActualConsumer(Channel channel) {
		super(channel);
		msgCount = 0;
	}

	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws java.io.IOException {
		String message = new String(body);
		
		System.out.println(message);
		++msgCount;
		getChannel().basicAck(envelope.getDeliveryTag(), false);
	}

	public int getMsgCount() {
		return msgCount;
	}
}
