package rmqexample;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.ReturnListener;



public class HandlingReturnListener implements ReturnListener  {

	@Override
	public void handleReturn(int replyCode, String replyText, String exchange, String routingKey,
			BasicProperties properties, byte[] body) throws IOException {
		
		System.out.println(replyText +":" +replyCode);
		System.out.println("********  UnHandled Message ***************");
		Book unHandledBook = Book.loadFromJSON(new String(body));
		System.out.println("Book id:" + unHandledBook.getBookID());
		System.out.println("Book Description:" + unHandledBook.getBookDescription());
		System.out.println("Book Author:" + unHandledBook.getAuthor());
		
		
		
	
		
	}
	

}
