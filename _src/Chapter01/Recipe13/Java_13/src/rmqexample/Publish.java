package rmqexample;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.tools.json.JSONWriter;

public class Publish {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="13"; 
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Handling unroutable messages **");
		
		String RabbitmqHost = "localhost";
		if (args.length > 0)
			RabbitmqHost = args[0];

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
	
		try 
		{
			Connection connection = factory.newConnection();
			System.out.println("Connected: " + RabbitmqHost);
			Channel channel = connection.createChannel();
			
			channel.addReturnListener(new HandlingReturnListener());
			String myExchange = "myUnroutableExchange_"+RECIPE_NR;
			channel.exchangeDeclare(myExchange, "direct", false, false, null);
			
		    int msgCuont=1;
			while (true){
				Book myBook = new Book();
			    myBook.setBookID(msgCuont);
			    myBook.setBookDescription("How to handling unroutable messages");
			    myBook.setAuthor("john doe");
			    String message = new JSONWriter().write(myBook);
			    boolean isMandatory = true; // if true the message will be handled by HandlingReturnListener
			    							// if false the message will be dropped! 
				

		        channel.basicPublish(myExchange, "",isMandatory,null, message.getBytes());
				System.out.println("Book: " + myBook.getBookID()  +" sent!");
				if (System.in.available() > 0) break;
				    Thread.sleep(1000);
				msgCuont++;    
		    }
			
			channel.close();
			System.out.println("Done.");
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ShutdownSignalException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
