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
		this.tie = false;
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
	public boolean hasSingleMoveTurns() {
		return true;
	}

	@Override
	public boolean gameIsOver() {
		int numBlack = gameState.numOnBoard(GlobalConstants.COLOR.BLACK);
		int numWhite = gameState.numOnBoard(GlobalConstants.COLOR.WHITE);
		return (numBlack + numWhite == gameState.getBoard().rows() * gameState.getBoard().columns() ||
				numBlack == 0 ||
				numWhite == 0
				);
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
		int numBlack = gameState.numOnBoard(GlobalConstants.COLOR.BLACK);
		int numWhite = gameState.numOnBoard(GlobalConstants.COLOR.WHITE);
		if(numWhite > numBlack){
			return players.get(0).get_id();
		} else if(numWhite < numBlack){
			return players.get(1).get_id();
		} else {
			this.tie = true;	
			return 0;
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
