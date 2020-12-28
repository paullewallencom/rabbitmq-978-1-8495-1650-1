package com.test.rmq;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.test.bean.RabbitMQInstance;






public class RmqWebSocket extends WebSocketServlet {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	private static final long serialVersionUID = 7181168464445168941L;
	private final Collection<ClientMessage> clients = new CopyOnWriteArrayList<ClientMessage>();
	private Channel channel=null;
	String consumerTag ="";

	public  RmqWebSocket()
	{
		logger.info("Init RmqWebSocket");
		 ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();  
         RabbitMQInstance rmq =  (RabbitMQInstance)ctx.getBean("rabbitMQInstance");  
		 if (rmq!=null){
			try {
				channel = rmq.getConnection().createChannel();
				channel.exchangeDeclare(com.test.util.Constants.exchange, "topic", false);
				logger.info("Exchange declared");
				String myQueue = channel.queueDeclare().getQueue();
				channel.queueBind(myQueue, com.test.util.Constants.exchange, com.test.util.Constants.routingKey);
				logger.info("Queue bound");
				ActualConsumer consumer = new ActualConsumer(channel);
				consumerTag = channel.basicConsume(myQueue, false, consumer);
				logger.info("Consumer started");
			} catch (IOException e) {
				logger.error("Error during get rabbitmq connection",e);
			}
			
			
		}
		else 
			logger.error("Rabbitmq instance is null");
	}

	@Override
	public void destroy(){
		if (channel!=null){
			try {
				channel.basicCancel(consumerTag);
				channel.close();
				logger.info("Channel closed");
			} catch (IOException e) {
				logger.error("Error during close rabbitmq channel",e);
			}
		}
		logger.info("Destroy RmqWebSocket");	
	}

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,HttpServletRequest arg1) {
		return new ClientMessage();
	}


	private class ClientMessage extends MessageInbound
	{

		private WsOutbound myoutbound;

		@Override
		public void onOpen(WsOutbound outbound){
			logger.info("Open new Client.");
			this.myoutbound = outbound;
			clients.add(this);

		}

		@Override
		public void onClose(int status){
			logger.info("Close Client.");
			clients.remove(this);
		}

		@Override
		public void onTextMessage(CharBuffer cb) throws IOException{

		}

		@Override
		public void onBinaryMessage(ByteBuffer bb) throws IOException{
		}


	}



	public class ActualConsumer extends DefaultConsumer {
		public ActualConsumer(Channel channel) {
			super(channel);
		}

		public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws java.io.IOException {
			String message = new String(body);
			logger.info("got:" + message);
			for(ClientMessage item: clients){
				CharBuffer buffer = CharBuffer.wrap(message);
				try {
					item.myoutbound.writeTextMessage(buffer);
					item.myoutbound.flush();
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}
			getChannel().basicAck(envelope.getDeliveryTag(), false);
		}


	}



}
