package rmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ActualConsumer extends DefaultConsumer {
	public ActualConsumer(Channel channel) {
		super(channel);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws java.io.IOException {
		String message = new String(body);
		MapController.INSTANCE.AddCmdFromJson(message);
	}
}
