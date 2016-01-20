package greycirclegames.frontend.views;

import java.util.LinkedList;
import java.util.List;

import greycirclegames.ArtificialPlayer;
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
	public GameEntryView(Game<? extends Move, ? extends GameState, ? extends ArtificialPlayer> game){
		gameId = game.get_id();
		currentPlayerId = game.getPlayers().get(game.currentPlayerIndex);
		if(currentPlayerId > 0){
			currentPlayerName = game.getCurrentPlayerObject().getUsername();
		} else {
			currentPlayerName = game.makeArtificialPlayerFromDB(currentPlayerId).getUsername();
		}
		for( Integer id : game.players){
			if(id < 0){
				players.add(game.makeArtificialPlayerFromDB(id).getUsername());
			} else {
				players.add(DBHandler.getUser(id).getUsername());
			}
		}
		this.isActive = game.getIsActive();
		
		if(!game.getIsActive()){
			Player user = DBHandler.getUser(game.getWinner_id());
			if(user != null){
				this.winner = user.getUsername();
			} else if (game.getTie()){
				this.winner = "It was a tie.";
			} else {
				this.winner = "A Computer Player";
			}
		}
	}
}
