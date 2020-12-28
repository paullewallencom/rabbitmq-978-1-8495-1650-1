package rmqexample;

import java.util.HashMap;

import com.rabbitmq.tools.json.JSONReader;

public class LastNews {

	private String newsText;
	private String newsKind;
	private String newsDate;

	public void setNewsText(String value) {
		newsText = value;
	}

	public String getNewsText() {
		return newsText;
	}

	public void setNewsKind(String value) {
		newsKind = value;
	}

	public String getNewsKind() {
		return newsKind;
	}

	public String getNewsDate() {
		return newsDate;
	}

	public void setNewsDate(String value) {
		newsDate = value;
	}
	
	
	public static LastNews LoadFromHasMap(HashMap<String, Object> HashNews){
		LastNews lastNews = new LastNews();
		lastNews.setNewsDate(HashNews.get("newsDate").toString());
		lastNews.setNewsKind(HashNews.get("newsKind").toString());
		lastNews.setNewsText(HashNews.get("newsText").toString());
		return lastNews;
		
}
	public static LastNews loadFromJSON(String JSONString){
		JSONReader jsonreader = new JSONReader();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> hashNews = (HashMap<String, Object>) jsonreader.read(JSONString);
		return LoadFromHasMap(hashNews);
	}

}
