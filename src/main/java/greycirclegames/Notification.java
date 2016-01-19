package greycirclegames;

import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

public class Notification extends ReflectionDBObject {
	private int uid;
	private String text;
	private String url;
	private int gameId;
	private boolean friends;	
	
	public Notification(Integer uid, String text, String url, int gameId, boolean friends){
		this.uid = uid;
		this.text = text;
		this.url = url;
		this.gameId = gameId;
		this.friends = friends;
	}
	
	public Notification(BasicDBObject obj){
		this.uid = (Integer)obj.get("Uid");
		this.text = (String)obj.getString("Text");
		this.url = (String)obj.getString("Url");
		this.gameId = (Integer)obj.get("GameId");
		this.friends = (Boolean)obj.get("Friends");
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
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public boolean getFriends() {
		return friends;
	}
	public void setFriends(boolean friends) {
		this.friends = friends;
	}
	
}
