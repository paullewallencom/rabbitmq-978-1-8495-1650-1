package rmqexample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class ActualConsumer extends DefaultConsumer {
	public ActualConsumer(Channel channel) {
		super(channel);
	}
	
	 
	private  void downloadUrl(String urltoDownload) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        System.out.println("Downloading... ");
        try {
        	URL url = new URL(urltoDownload);
        	URLConnection urlConn = url.openConnection();
         
            is = urlConn.getInputStream();
            String fileName = new File(urltoDownload).getName();
            // if you exec 2 or more  consumers in the same directory
            fileName = java.util.UUID.randomUUID().toString() +"_" +fileName;
            fos = new FileOutputStream(fileName);
            System.out.println("Write on: "+fileName);
            
            byte[] buffer = new byte[4096];
            int len;
            int linebreak=0;
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                linebreak+= buffer.length;
                if (linebreak >1024000){
                   System.out.print("*");
                   linebreak = 0;
                }
                
                
            }
            
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
        System.out.println("");
        System.out.println("Download compled!"); 
    }
	
	public void handleDelivery(
			 String consumerTag, 
			 Envelope envelope, 
			 BasicProperties properties, 
			 byte[] body) throws java.io.IOException {
  
		
		String messageURL = new String(body);
     System.out.println("Received: " + messageURL);
   
		System.out.println("Url to download:" + messageURL);
		downloadUrl(messageURL);
		getChannel().basicAck(envelope.getDeliveryTag(),false);
		System.out.println("Ack sent!");
		System.out.println("Wait for the next download...");
     
     
	}
}
