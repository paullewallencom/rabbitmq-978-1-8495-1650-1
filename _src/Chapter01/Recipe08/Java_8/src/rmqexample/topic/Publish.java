package rmqexample.topic;

import rmqexample.Book;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.tools.json.JSONWriter;

public class Publish {

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
		
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// To remember:
		// this example is linked with the example 8 chapter 2. 
		String RECIPE_NR="01/08"; 
		System.out.println(" ** RabbitmqCookBook - Recipe number "+RECIPE_NR+". Working with message routing (Client Java Topic-Publish) **");
		String RabbitmqHost = "localhost";
		if (args.length > 0)
			RabbitmqHost = args[0];

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RabbitmqHost);
		try {
			Connection connection = factory.newConnection();
			System.out.println("Conneted:" + RabbitmqHost);
			Channel channel = connection.createChannel();
			String myExchange = "myTopicExchangeRoutingKey_"+RECIPE_NR;
			channel.exchangeDeclare(myExchange, "topic", false, false, null);
			JSONWriter jsonw = new JSONWriter();
			
			// fist send with routingKey "technology.rabbitmq.ebook"
			String bookroutingKey="technology.rabbitmq.ebook";
			Book book = CreateBookInstance(1,"rabbitmq cookbook","technology","rabbitmq","ebook","G & S");
			String jsonBook = jsonw.write(book);
			channel.basicPublish(myExchange, bookroutingKey, null, jsonBook.getBytes());
			System.out.println("Sent 1 rounting key: " +bookroutingKey );
			 
			// second send with routingKey "sport.golf.paper"
			bookroutingKey="sport.golf.paper";
			book = CreateBookInstance(2,"Golf for beginner","sport","golf","paper","J. Doe");
			jsonBook = jsonw.write(book);
			channel.basicPublish(myExchange, bookroutingKey, null, jsonBook.getBytes());
			System.out.println("Sent 2 rounting key: " +bookroutingKey);
			
			//t send with routingKey "sport.tennis.ebook"
			bookroutingKey="sport.tennis.ebook";
			book = CreateBookInstance(3,"Tennis for beginner","sport","tennis","ebook","J. Doe");
			jsonBook = jsonw.write(book);
			channel.basicPublish(myExchange, bookroutingKey, null, jsonBook.getBytes());
			System.out.println("Sent 3 rounting key: " +bookroutingKey);
			
			channel.close();
			connection.close();
			System.out.println("Done.");
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
