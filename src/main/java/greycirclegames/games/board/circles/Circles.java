package greycirclegames.games.board.circles;

import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.games.Game;

public class Circles extends Game<CirclesMove, CirclesGameState, CirclesArtificialPlayer> {
	public static final List<String> turnColors = Arrays.asList(GlobalConstants.COLOR.WHITE, GlobalConstants.COLOR.BLACK);

	public Circles(int id, List<Player> players){
		super(id, players);
	}
	
	public Circles(DBObject obj){
		super.gameFromDBObject(obj);
		this.gameState = new CirclesGameState((BasicDBObject)obj.get("GameState"));
	}
	
	@Override
	protected CirclesGameState newGameState(List<Player> players) {
		CirclesGameState game = new CirclesGameState();
		game.initializeToNewGameState(players);
		return game;
	}

	@Override
	public String getGameTypeIdentifier() {
		return GlobalConstants.CIRCLES;
	}

	@Override
	public boolean gameIsOver() {
		CirclesBoard board = gameState.getBoard();
		for(int i = 0; i < board.rows(); i++){
			for(int j = 0; j < board.columns(); j++){
				if(board.cellAt(i, j) == null){
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
		CirclesBoard board = gameState.getBoard();
		for(int i = 0; i < board.rows(); i++){
			for(int j = 0; j < board.columns(); j++){
				if(board.cellAt(i, j).equals(GlobalConstants.COLOR.WHITE)){
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

	@Override
	protected CirclesArtificialPlayer makeArtificialPlayerFromDB(int playerId) {
		return new CirclesArtificialPlayer(playerId);
	}

	@Override
	protected CirclesMove makeMoveFromDB(BasicDBObject move) {
		return new CirclesMove(move);
	}

	@Override
	protected CirclesGameState makeGameStateFromDB(BasicDBObject dbObject) {
		return new CirclesGameState(dbObject);
	}
	
}
