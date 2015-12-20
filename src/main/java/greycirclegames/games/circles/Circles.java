package greycirclegames.games.circles;

import java.util.List;

import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;

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
		Circle[][] board = ((CirclesGameState) gameState).getBoard();
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j] == null){
					return false;
				}
			}
		}
		return true;
	}

}
