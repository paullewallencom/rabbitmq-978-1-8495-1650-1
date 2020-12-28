package rmqexample;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE]
 * 
 * 
 * Recipe number 1. Buffering data using messaging.
 * */


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.security.*;
import javax.net.ssl.*;
import java.io.*;
	
public class Publish {

	/**
	 * @param args
	 *            [0] RabbitmqHost
	 */
	public static void main(String[] args) {
		String RECIPE_NR="03/04";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Using SSL **");
		String hostname = "localhost";
		if (args.length > 0) {
			hostname = args[0];
		}

	

		try {
      char[] keyPassphrase = "client1234passwd".toCharArray();
      KeyStore ks = KeyStore.getInstance("PKCS12");
      ks.load(new FileInputStream("certificates/client/keycert.p12"), keyPassphrase);

      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, keyPassphrase);

      char[] trustPassphrase = "passwd1234".toCharArray();  
      KeyStore tks = KeyStore.getInstance("JKS");
      tks.load(new FileInputStream("certificates/keystore/rabbit.jks"), trustPassphrase);

      TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
      tmf.init(tks);

      SSLContext c = SSLContext.getInstance("SSLv3");
      c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
			
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(hostname);
			factory.setPort(5671);
			factory.useSslProtocol(c);
			Connection connection = factory.newConnection();


			System.out.println("Connected: " + hostname);
			Channel channel = connection.createChannel();
			
			String myQueue="queue_" + RECIPE_NR;
			boolean isDurable = true;
			boolean isExclusive = false;
			boolean isAutoDelete = false;
			// let's create our first queue
			channel.queueDeclare(myQueue, isDurable, isExclusive, isAutoDelete, null);
			
			String message;

			message = "just a message";
			channel.basicPublish("",myQueue, null ,message.getBytes());
			System.out.println("Message sent: " + message);
			
			channel.close();
			System.out.println("Finished.");
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
