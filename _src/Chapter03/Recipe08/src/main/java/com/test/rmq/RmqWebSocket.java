package com.test.rmq;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import com.test.bean.RabbitMQInstance;
@WebServlet("/websocket")


public class RmqWebSocket extends WebSocketServlet {

	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
 	
	private static final long serialVersionUID = 7181168464445168941L;
	private final Collection<ClientMessage> clients = new CopyOnWriteArrayList<ClientMessage>();
	SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

	

	@Override
	public void destroy(){
		container.stop();
		logger.info("Listener Stopped");
	}





	public RmqWebSocket(){
		logger.info("Init RmqWebSocket");
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();  
		RabbitMQInstance rmq =  (RabbitMQInstance)ctx.getBean("rabbitMQInstance");  
		if (rmq!=null){
			logger.info("got Rabbitmq Instance");


			container.setConnectionFactory( rmq.getConncetionFactory());
			container.setQueueNames(rmq.getBindQueue().getName());
			container.setAcknowledgeMode(AcknowledgeMode.AUTO);
			container.setMessageListener(new LogListener());
			container.start();
		}

		
	}

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,HttpServletRequest arg1) {
		return new ClientMessage();
	}


	private class LogListener implements MessageListener {

		@Override
		public void onMessage(Message message) {
			logger.info("got message:" + new String(message.getBody())) ;
			for(ClientMessage item: clients){
				CharBuffer buffer = CharBuffer.wrap(new String(message.getBody()) );
				try {

					item.myoutbound.writeTextMessage(buffer);
					item.myoutbound.flush();
				} catch (IOException e) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
			}

		}



	} 


	

	private class ClientMessage extends MessageInbound{
		WsOutbound myoutbound;

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
}
