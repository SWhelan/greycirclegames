package greycirclegames.games.circles;

import java.util.List;

import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;
import greycirclegames.games.kingscorner.Move;

public class Circles extends Game {

	public Circles(int id, List<Player> players){
		super(id, players);
	}
	
	@Override
	protected GameState newGameState(List<Player> players) {
		CirclesGameState game = new CirclesGameState();
		game.initializeToNewGameState(players);
		return game;
	}

	@Override
	protected String getGameTypeIdentifier() {
		return GlobalConstants.CIRCLES;
	}

	@Override
	public boolean gameIsOver() {
		return isActive;
	}

	private void setToWinState() {
		isActive = false;
		winner_id = players.get(gameState.getTurnNumber()).get_id();
	}

	@Override
	public boolean applyMove(Move move) {
		//If the game is not over and the move is valid.
		if(!gameIsOver() && move.isValid()){
			//Apply the move and save it to the list of moves
			move.apply();
			addMove(move);
			//Check if the move has made a player win
			if(gameIsOver()){
				setToWinState();
			}
			return true;
		}
		return false;
	}

}
