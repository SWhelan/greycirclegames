package greycirclegames.frontend.views;

import greycirclegames.DBHandler;
import greycirclegames.GameHistory;
import greycirclegames.GameHistoryEntry;
import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.User;

public class FriendView {
	public String username;
	public String email;
	public int numCirclesWin;
	public int numCirclesLost;
	public int numCirclesTie;
	public int numCirclesGame;
	
	public FriendView(int userId, Player friend){
		this.username = friend.getUsername();
		this.email = ((User) friend).getEmail();
		GameHistory history = DBHandler.getGameHistory(userId, friend.get_id());
		GameHistoryEntry circles = history.getEntryForGameType(GlobalConstants.CIRCLES);
		this.numCirclesLost = circles.getNumLost();
		this.numCirclesTie = circles.getNumTie();
		this.numCirclesWin = circles.getNumWon();
		this.numCirclesGame = circles.getNumGames();
	}
}
