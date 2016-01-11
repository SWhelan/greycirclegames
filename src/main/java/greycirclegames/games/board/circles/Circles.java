package greycirclegames.games.board.circles;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.games.Game;

public class Circles extends Game<CirclesMove, CirclesGameState, CirclesArtificialPlayer> {
	public List<String> turnColors;

	public Circles(int id, List<Player> players){
		super(id, players);
	}
	
	public Circles(DBObject obj){
		super.gameFromDBObject(obj);
		this.gameState = new CirclesGameState((BasicDBObject)obj.get("GameState"));
		turnColors = new ArrayList<String>();
		turnColors.add(GlobalConstants.COLOR.WHITE);
		turnColors.add(GlobalConstants.COLOR.BLACK);
	}
	
	@Override
	protected CirclesGameState newGameState(List<Player> players) {
		CirclesGameState game = new CirclesGameState();
		game.initializeToNewGameState(players);
		turnColors = new ArrayList<String>();
		turnColors.add(GlobalConstants.COLOR.WHITE);
		turnColors.add(GlobalConstants.COLOR.BLACK);
		return game;
	}

	@Override
	protected String getGameTypeIdentifier() {
		return GlobalConstants.CIRCLES;
	}

	@Override
	public boolean gameIsOver() {
		String[][] board = ((CirclesGameState) gameState).getBoard();
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

	public boolean endTurn() {
		if(gameIsOver()){
			super.changeToWinState();
		} else {
			currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
		}
		return true;
	}

	@Override
	protected int determineWinnerId() {
		int light = 0;
		int dark = 0;
		String[][] board = ((CirclesGameState) gameState).getBoard();
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j].equals(GlobalConstants.COLOR.WHITE)){
					light++;
				} else {
					dark++;
				}
			}
		}
		if(light > dark){
			return players.get(0).get_id();
		} else {
			return players.get(1).get_id();
		}
	}

}
