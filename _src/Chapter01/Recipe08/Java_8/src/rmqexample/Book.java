package rmqexample;

import java.util.HashMap;

import com.rabbitmq.tools.json.JSONReader;


public class Book {
	private int bookID;
	private String bookDescription;
	private String author;
	////
	private String argoument;
	private String format;
	private String bookKind; 

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

	public void setBookKind(String value) {
		bookKind = value;
	}

	public String getBookKind() {
		return bookKind;
	}
	
	public void setArgoument(String value){
		argoument = value;
	}
	
	public String getArgoument(){
		return argoument;
	}
	
	public void setFormat(String value){
		format = value;
	}
	
	public String getFormat(){
		return format;
	}
	
	public static Book LoadFromHasMap(HashMap<String, Object> HashBook){
		Book book = new Book();
		book.setBookID(Integer.parseInt(HashBook.get("bookID").toString()));
		book.setBookDescription(HashBook.get("bookDescription").toString());
		book.setAuthor(HashBook.get("author").toString());
		book.setBookKind(HashBook.get("bookKind").toString());
		book.setArgoument(HashBook.get("argoument").toString());
		book.setFormat(HashBook.get("format").toString());
		return book;
		
	}
	public static Book loadFromJSON(String JSONString){
		JSONReader jsonreader = new JSONReader();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> hashBook=  (HashMap<String, Object>) jsonreader.read(JSONString);
		return LoadFromHasMap(hashBook);
	}


}
