package greycirclegames.games.board.circles;

import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

public class CirclePiece extends ReflectionDBObject {
	private String name;
	private String hex;
	
	public CirclePiece(String name, String hex){
		this.name = name;
		this.hex = hex;
	}
	
	public CirclePiece(BasicDBObject obj){
		this.name = (String)obj.get("Name");
		this.hex = (String)obj.get("Hex");
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getHex(){
		return hex;
	}
	
	public void setHex(String hex){
		this.hex = hex;
	}
}
