package greycirclegames;

import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

public class Notification extends ReflectionDBObject {
	private String text;
	private String url;
	
	public Notification(String text, String url){
		this.text = text;
		this.url = url;
	}
	
	public Notification(BasicDBObject obj){
		this.text = (String)obj.getString("Text");
		this.text = (String)obj.getString("Url");
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
