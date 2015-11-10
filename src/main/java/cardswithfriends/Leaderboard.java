package cardswithfriends;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;

public class Leaderboard {
	//User id --> GameType ID --> #wins, # losses in tht game
	public Map<User, Map<String, Integer[]>> gameStats;
	
	public Leaderboard(User user){
		gameStats = new HashMap<User, Map<String,Integer[]>>();
		List<User> friends = user.getFriendsList();
		friends.add(user);
		
		BasicDBObject userStats;
		Map<String, Integer[]> wlmap;
		for(User u : friends){
			userStats = u.getWinsAndLosses();
			wlmap = new HashMap<String, Integer[]>();
			for(Entry<String, Object> e : userStats.entrySet()){
				wlmap.put(e.getKey(), (Integer[])e.getValue());
			}
			gameStats.put(u, wlmap);
		}
	}
}
