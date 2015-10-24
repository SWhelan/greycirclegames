package cardswithfriends;

import java.io.Serializable;
import java.util.Map;

public class Leaderboard implements Serializable{
	private static final long serialVersionUID = 1L;
	public Map<Integer, Integer[]> gameStats;
	public Map<User, Integer[]> friendStats;
}
