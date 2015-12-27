package greycirclegames.frontend.views;

import java.util.LinkedList;
import java.util.List;

import greycirclegames.DBHandler;
import greycirclegames.Player;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;
import greycirclegames.games.Move;

public class GameEntryView {
	public boolean isActive;
	public String winner;
	public int gameId;
	public String currentPlayerName;
	public int currentPlayerId;
	public List<String> players = new LinkedList<String>();
	public GameEntryView(Game<? extends Move, ? extends GameState> game){
		gameId = game.get_id();
		currentPlayerName = game.getCurrentPlayerObject().getUserName();
		currentPlayerId = game.getCurrentPlayerObject().get_id();
		for(Object p : game.players){
			players.add(((Player)p).getUserName());
		}
		this.isActive = game.getIsActive();
		
		if(!game.getIsActive()){
			Player user = DBHandler.getUser(game.getWinner_id());
			if(user != null){
				this.winner = user.getUserName();
			} else {
				this.winner = "A Computer Player";
			}
		}
	}
}
