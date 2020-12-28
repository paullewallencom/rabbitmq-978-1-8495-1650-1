package rmqexample;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ActualConsumer extends DefaultConsumer {
	private int msgCount;
	
	public ActualConsumer(Channel channel) {
		super(channel);
		msgCount=0;
	}

	public void handleCancel(String consumerTag) throws IOException {
		System.out.println("CANCEL received - it is possible that queue \"" + Constants.queue + "\" no longer exist");
	}
	
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws java.io.IOException {
		String message = new String(body);
		Stats stats = Stats.loadFromJSON(message);
		System.out.println(stats.toString());
		++msgCount;
		getChannel().basicAck(envelope.getDeliveryTag(), false);
	}
	
	public int getMsgCount() {
		return msgCount;
	}
}
