package rmqexample;



import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ActualConsumer extends DefaultConsumer {
	public ActualConsumer(Channel channel) {
		super(channel);
	}
	
	MqttClientInterface mqttClientInterface = null;

	public MqttClientInterface getMqttClientInterface() {
		return mqttClientInterface;
	}

	public void setMqttClientInterface(MqttClientInterface mqttClientInterface) {
		this.mqttClientInterface = mqttClientInterface;
	}

	public void handleDelivery(
			 String consumerTag, 
			 Envelope envelope, 
			 BasicProperties properties, 
			 byte[] body) throws java.io.IOException {

		
		String msg = new String(body);
		System.out.println("Received: " +  msg);
		if (mqttClientInterface!= null)
			try {
				System.out.println("Sending to MQTT...");
				mqttClientInterface.PublishToMQTTserver(body);
				System.out.println("Message Sent!");
			} catch (MqttPersistenceException e) {
				e.printStackTrace();
			} catch (MqttException e) {
				e.printStackTrace();
			}
	}
}
