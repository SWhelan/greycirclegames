package greycirclegames;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

public class GameHistory extends ReflectionDBObject {
	private int userId1;
	private int userId2;
	private List<GameHistoryEntry> entries;
	
	public GameHistory(){
		// Mongo DB
	}
	
	public GameHistory(int userId1, int userId2){
		this.userId1 = userId1;
		this.userId2 = userId2;
		this.entries = new LinkedList<GameHistoryEntry>();
		entries.add(new GameHistoryEntry(GlobalConstants.KINGS_CORNER, 0, 0, 0, 0));
		entries.add(new GameHistoryEntry(GlobalConstants.CIRCLES, 0, 0, 0, 0));
	}
	
	public GameHistory(BasicDBObject obj){
		this.userId1 = (Integer) obj.get("UserId1");
		this.userId2 = (Integer) obj.get("UserId2");
		BasicDBList entries = (BasicDBList) obj.get("Entries");
		for(Object entry : entries){
			this.entries.add(new GameHistoryEntry((BasicDBObject) entry));
		}
	}
	
	public int getUserId1() {
		return userId1;
	}
	public void setUserId1(int userId1) {
		this.userId1 = userId1;
	}
	public int getUserId2() {
		return userId2;
	}
	public void setUserId2(int userId2) {
		this.userId2 = userId2;
	}
	public List<GameHistoryEntry> getEntries() {
		return entries;
	}
	public void setEntries(List<GameHistoryEntry> entries) {
		this.entries = entries;
	}
	
	public void addEntry(GameHistoryEntry entry){
		this.entries.add(entry);
	}
	
	public GameHistoryEntry getEntryForGameType(String gameIdentifier){
		List<GameHistoryEntry> matches = this.entries.stream().filter(e -> e.getGameIdentifier().equals(gameIdentifier)).collect(Collectors.toList());
		if(matches.size() > 0){
			return matches.get(0);
		} else {
			return null;
		}
	}
	
}
