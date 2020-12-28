package rmqexample;

import java.util.HashMap;

import com.rabbitmq.tools.json.JSONReader;


public class Book {
	private int bookID;
	private String bookDescription;
	private String author;


	public void setBookID(int value) {
		bookID = value;
	}

	public int getBookID() {
		return bookID;
	}

	public void setBookDescription(String value) {
		bookDescription = value;
	}

	public String getBookDescription() {
		return bookDescription;
	}

	public void setAuthor(String value) {
		author = value;
	}

	public String getAuthor() {
		return author;
	}


	
	public static Book LoadFromHasMap(HashMap<String, Object> HashBook){
		Book book = new Book();
		book.setBookID(Integer.parseInt(HashBook.get("bookID").toString()));
		book.setBookDescription(HashBook.get("bookDescription").toString());
		book.setAuthor(HashBook.get("author").toString());
		return book;
		
	}
	public static Book loadFromJSON(String JSONString){
		JSONReader jsonreader = new JSONReader();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> hashBook=  (HashMap<String, Object>) jsonreader.read(JSONString);
		return LoadFromHasMap(hashBook);
	}


}
