package rmqexample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class Publish {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String RECIPE_NR="10"; 
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Distributing messages to many consumers**");
		String hostname = "localhost";
		if (args.length > 0) {
			hostname = args[0];
		}
		
		String messageUrlToDownload =  "http://www.rabbitmq.com/releases/rabbitmq-dotnet-client/v3.0.2/rabbitmq-dotnet-client-3.0.2-user-guide.pdf";
	
		if (args.length > 1) {
			messageUrlToDownload = args[1];
			// in order to use this parameter you have to set also the rabbitmq host.
			// for example com.rabbitmqcookbook.Publish localhost "http://www.rabbitmq.com/releases/rabbitmq-dotnet-client/v3.0.2/rabbitmq-dotnet-client-3.0.2-user-guide.pdf"
		}
		
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(hostname);
		try {
			Connection connection = factory.newConnection();

			System.out.println("Connected: " + hostname);
			Channel channel = connection.createChannel();
			
			String myQueue="myUrlDownloadQueue_"+RECIPE_NR;
			boolean isDurable = false;
			boolean isExclusive = false;
			boolean isAutoDelete = false;
			channel.queueDeclare(myQueue, isDurable, isExclusive, isAutoDelete, null);
			channel.basicPublish("",myQueue, null,messageUrlToDownload.getBytes());
			System.out.println("Done!");
			
			channel.close();
			System.out.println("Finished.");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	

}
