package rmqexample;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;


public class MqttClientInterface {

	/**
	 * @param args
	 */
	MqttClient mqttClient = null;
	public void Connect(String MQTTip) throws MqttException{
		mqttClient = new MqttClient(MQTTip, MqttClient.generateClientId());
	  	mqttClient.connect();
	}
	
	public void PublishToMQTTserver(byte[] message) throws MqttPersistenceException, MqttException{
		mqttClient.publish("technology.rabbitmq.ebook", message, 0, false);
	}
	
	public void Disconnect() throws MqttException{
		mqttClient.disconnect();
	}
	
	

}
