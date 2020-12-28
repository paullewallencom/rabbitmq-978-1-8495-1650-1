package rmqexample;

import java.util.HashMap;

import com.rabbitmq.tools.json.JSONReader;

public class Order {
		
	private int userID;
	private int bookID;
	private String address;
	
	
	public int getUserID() {
		return userID;
	}


	public void setUserID(int userID) {
		this.userID = userID;
	}


	public int getBookID() {
		return bookID;
	}


	public void setBookID(int bookID) {
		this.bookID = bookID;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String adress) {
		this.address = adress;
	}


	public static Order loadFromJSON(String JSONString) {
		JSONReader jsonreader = new JSONReader();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> hash=  (HashMap<String, Object>) jsonreader.read(JSONString);
		
		Order order = new Order();
		order.bookID = Integer.valueOf(hash.get("bookID").toString());
		order.userID = Integer.valueOf(hash.get("userID").toString());
		order.address = hash.get("address").toString();
		return order;
	}
	
	
}
