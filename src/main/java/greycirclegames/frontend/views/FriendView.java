package greycirclegames.frontend.views;

import greycirclegames.DBHandler;
import greycirclegames.GameHistory;
import greycirclegames.GameHistoryEntry;
import greycirclegames.GlobalConstants;
import greycirclegames.Player;

public class FriendView {
	public String username;
	public int numCirclesWin = 0;
	public int numCirclesLost = 0;
	public int numCirclesTie = 0;
	public int numCirclesGames = 0;
	public int numKCWin = 0;
	public int numKCLost = 0;
	public int numKCTie = 0;
	public int numKCGames = 0;
	
	public FriendView(int userId, Player friend){
		this.username = friend.getUsername();
		GameHistory history = DBHandler.getGameHistory(userId, friend.get_id());
		if(history != null){
			GameHistoryEntry circles = history.getEntryForGameType(GlobalConstants.CIRCLES);
			this.numCirclesLost = circles.getNumLost();
			this.numCirclesTie = circles.getNumTie();
			this.numCirclesWin = circles.getNumWon();
			this.numCirclesGames = circles.getNumGames();
			GameHistoryEntry kc = history.getEntryForGameType(GlobalConstants.KINGS_CORNER);
			this.numKCLost = kc.getNumLost();
			this.numKCTie = kc.getNumTie();
			this.numKCWin = kc.getNumWon();
			this.numKCGames = kc.getNumGames();
		}
	}
}
