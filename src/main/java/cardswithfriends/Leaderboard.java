package cardswithfriends;

import java.util.Map;

import com.mongodb.ReflectionDBObject;

public class Leaderboard extends ReflectionDBObject{
	public Map<Integer, Integer[]> gameStats;
	//key is the userID
	public Map<Integer, Integer[]> friendStats;
}
