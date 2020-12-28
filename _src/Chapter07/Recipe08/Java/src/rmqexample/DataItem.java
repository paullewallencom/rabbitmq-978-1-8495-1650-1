package rmqexample;

public class DataItem {
	private String data;
	private long id;

	public DataItem(String data, long id) {
		this.id = id;
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public long getId() {
		return id;
	}
}
