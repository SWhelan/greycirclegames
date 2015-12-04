package cardswithfriends;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

public class Leaderboard extends ReflectionDBObject{
	/**
	 * User id --> GameType ID --> #wins, # losses in that game
	 * The embedded basicDBobject is GameType ID's (only one in our case) mapped to a BasicDBList
	 * The embedded basicDBList has length 2, first entry contains # of wins, second contains # of losses
	 */
	public Map<String, BasicDBObject> gameStats;
	
	/**
	 * Default constructor, initialize empty map
	 */
	public Leaderboard() {
		gameStats = new HashMap<String, BasicDBObject>();
	}

	/**
	 * Builds the global leaderboard.
	 * @param obj
	 */
	public Leaderboard(BasicDBObject obj) {
		this.gameStats = new HashMap<String, BasicDBObject>();
		
		BasicDBObject gameStats = (BasicDBObject)obj.get("GameStats");
		for (Entry<String, Object> e : gameStats.entrySet()) {
			if(Integer.parseInt(e.getKey()) >= 0){
				this.gameStats.put(e.getKey(), (BasicDBObject)e.getValue());
			}
		}
	}
	
	/**
	 * Builds the leaderboard for a user - a leaderboard which only contains
	 * the given user's friends, and themself.
	 * Currently, there is no front-end display of this.
	 * @param user
	 */
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
