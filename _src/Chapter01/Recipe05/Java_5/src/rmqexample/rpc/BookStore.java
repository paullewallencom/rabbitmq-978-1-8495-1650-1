package rmqexample.rpc;

import java.util.Map;
import java.util.TreeMap;

public class BookStore {
	Map<Integer,Book> books;
	
	BookStore() {
		books = new TreeMap<Integer,Book>();
		// we fill the bookstore with some example data.
		for (int i = 1; i < 11; i++) {
			Book book = new Book();
			book.setBookID(i);
			book.setBookDescription("History VOL: " + i);
			book.setAuthor("John Doe");
			books.put(i,book);
		}
	}

	Book GetBook(int id) {
		return books.get(id);
	}
}
