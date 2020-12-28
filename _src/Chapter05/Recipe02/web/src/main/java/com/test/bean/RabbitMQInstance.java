package com.test.bean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.test.utils.Constants;



/**
 * 
 * @author gabriele
 *
 * Check out the root-context.xml file to change the parameters.
 */

public class RabbitMQInstance {
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQInstance.class);
	
	
	  
	
	
	
	ConnectionFactory factory = new ConnectionFactory();
	Connection connection = null;
	private String rabbitMQhost;// <property name="rabbitMQhost" value="localhost"/> in root-context.xml

	private String lastError="";
	public String getLastError() {
		return lastError;
	}


	public Connection getConnection() {
		return connection;
	}


	public void setConnection(Connection connection) {
		this.connection = connection;
	}

  	
	private Channel channel=null;
	private String consumerTag ="";
	
	/**
	 * the UUIDAPPLICATION identify the module.
	 * Used as routing-key to bind a module to the com.test.utils.Constants.exchange. 
	 */
	private String UUIDAPPLICATION="";
	@PostConstruct   
	public void Init(){
		UUIDAPPLICATION = java.util.UUID.randomUUID().toString();
		lastError ="";
		factory.setHost(rabbitMQhost);
		try {
			logger.info("Connection to RabbitMQ..."+  rabbitMQhost);
			connection = factory.newConnection();
			logger.info("Connected!");
			
			channel = getConnection().createChannel();
			channel.exchangeDeclare(com.test.utils.Constants.exchange, "topic", false);
			logger.info("Exchange declared: " +com.test.utils.Constants.exchange);

			// here we put a time out message, if the message is present for more than 4 second must be deleted
			// the request can't be execute, it means that there is some problem
			// The http request wait only for 3 second.
			// check out the HomeController function searchbook 
			//DeferredResult<String> deferredResult = new DeferredResult<String>(3000);<--this is the timeout request
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("x-message-ttl", 4000);
			channel.queueDeclare(Constants.queue, false, false, false, args);
			logger.info("Queue declared: " +com.test.utils.Constants.queue);
			// in production you should handle the expire messages 
			
			String myQueue = channel.queueDeclare().getQueue();
			///
			channel.queueBind(myQueue, com.test.utils.Constants.exchange, UUIDAPPLICATION);
			logger.info("Queue bound");
			SearchResultConsumer consumer = new SearchResultConsumer(channel);
			consumerTag = channel.basicConsume(myQueue, false, consumer);
			logger.info("Consumer started");
			
		} catch (IOException e) {
			lastError ="Error during Rabbitmq connection: "+ e.getMessage();
			logger.error(e.getMessage());
		}
	}


	@PreDestroy
	public void Deinit(){
		try{
			channel.basicCancel(consumerTag);
			channel.close();
			connection.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public String getRabbitMQhost() {
		return rabbitMQhost;
	}

	public void setRabbitMQhost(String rabbitMQhost) {
		this.rabbitMQhost = rabbitMQhost;
	}
	
	
	
	/**
	 * requests contains all http requests.
	 *    
	 */
	public	 ConcurrentHashMap<String, DeferredResult<String>> requests = new ConcurrentHashMap<String, DeferredResult<String>>();
	
	public void makeRequest(String uuidrequest,String bookkey,DeferredResult<String> deferredResult){
	
		try {
			requests.put(uuidrequest, deferredResult);
			Channel channelPub =  connection.createChannel();
			AMQP.BasicProperties.Builder bob = new AMQP.BasicProperties.Builder();
			Map<String, Object> header = new HashMap<String,Object> ();
			header.put("uuidrequest", uuidrequest); // guidRequest will be used when the result-set is back to the webserver to find the request on requests map
			bob.headers(header);
			bob.correlationId(UUIDAPPLICATION); // will be used from the java back-end module to publish a message with the routing-key. Route the message to original sender  
			try{
				channelPub.basicPublish("",com.test.utils.Constants.queue,  bob.build(), bookkey.getBytes());
			}finally{
				channelPub.close();
			}
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	
	public class SearchResultConsumer extends DefaultConsumer {
		public SearchResultConsumer(Channel channel) {
			super(channel);
		}

		/**
		 * Handle the result-set
		 *  
		 */
		public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws java.io.IOException {
			String jsonResultSet = new String(body);
			try {
				Object uuidrequest = properties.getHeaders().get("uuidrequest"); // the guid request is back 
				DeferredResult<String>  deferredResult = requests.get(uuidrequest.toString()); // try to find the request
				if (deferredResult!=null){
					//	logger.info("Setting deferredResult: " + jsonResultSet);
					deferredResult.setResult(jsonResultSet); // if found set the result. and the data are finally to the browser.
				} else 
					logger.error("DeferredResult is null, the request could be removed for time out"); 
				requests.remove(uuidrequest.toString()); // the request is removed from the requests map.
				logger.info(" you have " + requests.size() +" pending requests");
				
			} catch (Exception e) {
				e.printStackTrace();
 			} 
			getChannel().basicAck(envelope.getDeliveryTag(), false);
			/**
			 * To make faster the handler you can use a ThreadPool, that takes the message, find the request and set the result.
			 */
		}
	}
}
