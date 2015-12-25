package greycirclegames.games.board.circles;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;

public class Circles extends Game {
	public List<Circle> turnColors;

	public Circles(int id, List<Player> players){
		super(id, players);
	}
	
	public Circles(DBObject obj){
		super.gameFromDBObject(obj);
		this.gameState = new CirclesGameState((BasicDBObject)obj.get("GameState"));
		turnColors = new ArrayList<Circle>();
		turnColors.add(new Circle("Light", "#ffffff"));
		turnColors.add(new Circle("Dark", "#000000"));
	}
	
	@Override
	protected GameState newGameState(List<Player> players) {
		CirclesGameState game = new CirclesGameState();
		game.initializeToNewGameState(players);
		turnColors = new ArrayList<Circle>();
		turnColors.add(new Circle("Light", "#ffffff"));
		turnColors.add(new Circle("Dark", "#000000"));
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
	
	public boolean applyAIMoves() {
		// TODO Auto-generated method stub
		return false;
	}

	public void makeMove(int row, int column, Circle color) {
		CirclesMove move = new CirclesMove(row, column, color, (CirclesGameState) this.getGameState());
		if(move.isValid()){
			move.apply();
		}
	}

}
