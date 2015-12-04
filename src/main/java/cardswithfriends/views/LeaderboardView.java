package cardswithfriends.views;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import cardswithfriends.DBHandler;
import cardswithfriends.GlobalConstants;
import cardswithfriends.Leaderboard;
import cardswithfriends.Player;

public class LeaderboardView {
	public List<LeaderboardEntryView> entries;
	
	public static Comparator<LeaderboardEntryView> entryComparator = 
			(LeaderboardEntryView entry1, LeaderboardEntryView entry2) -> entry1.compareTo(entry2);
	
	public LeaderboardView(){
		Leaderboard leaderboard = DBHandler.getLeaderboard();
		entries = leaderboard.gameStats.entrySet().stream().map(e -> {
			int wins;
			int losses;
			Player user;			
			user = DBHandler.getUser(Integer.parseInt(e.getKey()));
			BasicDBObject gameEntries = e.getValue();
			BasicDBList kingsCornerNumbers = (BasicDBList) gameEntries.get(GlobalConstants.KINGS_CORNER);
			wins = (Integer)kingsCornerNumbers.get(0);
			losses = (Integer)kingsCornerNumbers.get(1);
			return new LeaderboardEntryView(wins, losses, user);
		})
		.filter(e -> e.user != null)
		.collect(Collectors.toList());
		
		entries.sort(entryComparator);
	}
}
