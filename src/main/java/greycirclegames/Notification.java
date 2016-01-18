package greycirclegames;

import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

public class Notification extends ReflectionDBObject {
	private int uid;
	private String text;
	private String url;
	
	public Notification(Integer uid, String text, String url){
		this.uid = uid;
		this.text = text;
		this.url = url;
	}
	
	public Notification(BasicDBObject obj){
		this.uid = (Integer)obj.get("Uid");
		this.text = (String)obj.getString("Text");
		this.url = (String)obj.getString("Url");
	}
	
	public int getUid(){
		return this.uid;
	}
	public void setUid(Integer uid){
		this.uid = uid;
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
