package cardswithfriends;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

public class Leaderboard extends ReflectionDBObject{
	//User id --> GameType ID --> #wins, # losses in that game
	//The embedded basicDBobject is GameType ID's (only one in our case) mapped to a BasicDBList
	//The embedded basicDBList has length 2, first entry contains # of wins, second contains # of losses
	public Map<String, BasicDBObject> gameStats;
	
	//deafult contructor, initialize empty map
	public Leaderboard() {
		gameStats = new HashMap<String, BasicDBObject>();
	}

	public Leaderboard(BasicDBObject obj) {
		this.gameStats = new HashMap<String, BasicDBObject>();
		
		BasicDBObject gameStats = (BasicDBObject)obj.get("GameStats");
		for (Entry<String, Object> e : gameStats.entrySet()) {
			this.gameStats.put(e.getKey(), (BasicDBObject)e.getValue());
		}
	}
	
	public Leaderboard(User user){
		gameStats = new HashMap<String, BasicDBObject>();
		List<User> friends = user.getFriendsList();
		friends.add(user);
		
		BasicDBObject userStats;
		for(User u : friends){
			userStats = u.getWinsAndLosses();
			gameStats.put(Integer.toString(u.get_id()), userStats);
		}
	}
	
	public void addUser(User user){
			gameStats.put(Integer.toString(user.get_id()), user.getWinsAndLosses());
	}

	public Map<String, BasicDBObject> getGameStats() {
		return gameStats;
	}

	public void setGameStats(Map<String, BasicDBObject> gameStats) {
		this.gameStats = gameStats;
	}
}
