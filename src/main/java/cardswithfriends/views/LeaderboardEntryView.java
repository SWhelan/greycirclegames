package cardswithfriends.views;

import cardswithfriends.Player;

public class LeaderboardEntryView implements Comparable<LeaderboardEntryView>{
	public int numWins;
	public int numLosses;
	public Player user;
	
	public LeaderboardEntryView(int numWins, int numLosses, Player user){
		this.numWins = numWins;
		this.numLosses = numLosses;
		this.user = user;
	}
	
	@Override
	public int compareTo(LeaderboardEntryView o) {
		LeaderboardEntryView other = (LeaderboardEntryView) o;
		if(other.numWins == this.numWins){
			return 0;
		} else if (other.numWins > this.numWins){
			return -1;
		} else {
			return 1;
		}
	}

}
