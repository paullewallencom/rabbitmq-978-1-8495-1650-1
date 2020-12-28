package rmqexample.direct;

import rmqexample.Book;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.json.JSONWriter;

/*
 * RabbitmqCookBook [TAG_TO_REPLACE] 
 * 
 */

public class Publish {

	/**
	 * @param args
	 */
	private static Book CreateBookInstance(int id,String description,String kind,String argoument,String format,String author){
		Book book = new Book();
		book.setBookID(id);
		book.setBookDescription(description);
		book.setArgoument(argoument);
		book.setBookKind(kind);
		book.setFormat(format);
		book.setAuthor(author);
		return book;
	
	}
	
	public static void main(String[] args) {
		String RECIPE_NR="7";
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Working with message routing (Client Java Direct-Publish) **");
		String rabbitmqHost = "localhost";
		if (args.length > 0)
			rabbitmqHost = args[0];

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(rabbitmqHost);
		try {
			Connection connection = factory.newConnection();
			System.out.println("Conneted:" + rabbitmqHost);
			Channel channel = connection.createChannel();
			String myExchange = "myDirectExchangeRoutingKey_"+ RECIPE_NR;
			channel.exchangeDeclare(myExchange, "direct", false, false, null);
			JSONWriter jsonw = new JSONWriter();
			
			// fist send with routingKey "technology"
			String routingKey="technology";
			Book book = CreateBookInstance(1,"rabbitmq cookbook",routingKey,"rabbitmq","ebook","G & S");
			String jsonBook = jsonw.write(book);
			channel.basicPublish(myExchange, routingKey, null, jsonBook.getBytes());
			System.out.println("Sent 1 rounting key: " +routingKey );
			
			// second send with routingKey "sport"
			routingKey="sport";
			book = CreateBookInstance(2,"Golf for beginner",routingKey,"golf","paper","J. Doe");
			jsonBook = jsonw.write(book);
			channel.basicPublish(myExchange, routingKey, null, jsonBook.getBytes());
			System.out.println("Sent 2 rounting key: " +routingKey);
			
			//t send with routingKey "sport"
			routingKey="sport";
			book = CreateBookInstance(3,"Tennis for beginner",routingKey,"tennis","ebook","J. Doe");
			jsonBook = jsonw.write(book);
			channel.basicPublish(myExchange, routingKey, null, jsonBook.getBytes());
			System.out.println("Sent 3 rounting key: " +routingKey);
			
			channel.close();
			connection.close();
			System.out.println("Done.");
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
