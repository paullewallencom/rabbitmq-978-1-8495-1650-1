package rmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQMQttProxy {

	/**
	 * @param args
	 */
	
	ConnectionFactory factory = new ConnectionFactory();
	MqttClientInterface mqttClientInterface = new MqttClientInterface();
	private void Start(String RabbitmqHost,String MQTTip){
		
		factory.setHost(RabbitmqHost);
		try {
			mqttClientInterface.Connect(MQTTip);
			Connection connection = factory.newConnection();
			
			System.out.println("RabbitMQ Host: " + RabbitmqHost);
			System.out.println("MQTT Host: " + MQTTip);
			Channel channel = connection.createChannel();
			String myQueue = channel.queueDeclare().getQueue();
			String myExchange = "amq.fanout";
			channel.queueBind(myQueue,myExchange,"");
			ActualConsumer consumer = new ActualConsumer(channel);
			consumer.setMqttClientInterface(mqttClientInterface);
			boolean autoAck = true; 
			String consumerTag = channel.basicConsume(myQueue, autoAck, consumer);
			System.out.println("Proxy Ready - press a key to terminate");
			System.in.read();
			
			channel.basicCancel(consumerTag);
			mqttClientInterface.Disconnect();
			channel.close();
			connection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 

		
		
		
	}
	
	
	public static void main(String[] args) {
		String RECIPE_NR="10"; 
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR +". RabbitMQ - MQtt Proxy **");
		String RabbitmqHost = "localhost";
		String MQTTip = "tcp://test.mosquitto.org:1883";

		if (args.length > 0)
			RabbitmqHost = args[0];
		
		if (args.length > 1)
			MQTTip = args[1];
		
		
		new RabbitMQMQttProxy().Start(RabbitmqHost,MQTTip);
		


	}

}
