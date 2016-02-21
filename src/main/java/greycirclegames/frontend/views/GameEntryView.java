package greycirclegames.frontend.views;

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
	public String gameRoute;
	public String players;
	public GameEntryView(Game<? extends Move, ? extends GameState, ? extends ArtificialPlayer> game, String gameRoute){
		gameId = game.get_id();
		currentPlayerId = game.getPlayers().get(game.currentPlayerIndex);
		if(currentPlayerId > 0){
			currentPlayerName = game.getCurrentPlayerObject().getUsername();
		} else {
			currentPlayerName = game.makeArtificialPlayer(currentPlayerId).getUsername();
		}
		StringBuilder builder = new StringBuilder();
		for( Integer id : game.players){
			if(id < 0){
				builder.append(game.makeArtificialPlayer(id).getUsername());
			} else {
				builder.append(DBHandler.getUser(id).getUsername());
			}
			builder.append(", ");
		}
		builder.delete(builder.length()-2, builder.length());
		this.players = builder.toString();
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
		this.gameRoute = gameRoute;
	}
}
