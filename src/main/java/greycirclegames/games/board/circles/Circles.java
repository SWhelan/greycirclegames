package greycirclegames.games.board.circles;

import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import greycirclegames.GlobalConstants;
import greycirclegames.frontend.TemplateHandler;
import greycirclegames.games.Game;

public class Circles extends Game<CirclesMove, CirclesGameState, CirclesArtificialPlayer> {
	public static final List<String> turnColors = Arrays.asList(GlobalConstants.COLOR.WHITE, GlobalConstants.COLOR.BLACK);

	public Circles(int id, List<Integer> players){
		super(id, players);
	}
	
	public Circles(DBObject obj){
		super.gameFromDBObject(obj);
	}
	
	@Override
	protected CirclesGameState newGameState(List<Integer> players) {
		CirclesGameState state = new CirclesGameState();
		state.initializeToNewGameState(this, players);
		return state;
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

	@Override
	public boolean applyEndTurn() {
		return true;
	}

	@Override
	protected int determineWinnerId() {
		int numBlack = gameState.numOnBoard(GlobalConstants.COLOR.BLACK);
		int numWhite = gameState.numOnBoard(GlobalConstants.COLOR.WHITE);
		if(numWhite > numBlack){
			return players.get(0);
		} else if(numWhite < numBlack){
			return players.get(1);
		} else {
			this.tie = true;	
			return 0;
		}
	}

	@Override
	public CirclesArtificialPlayer makeArtificialPlayerFromDB(int playerId) {
		return new CirclesArtificialPlayer(playerId);
	}

	@Override
	public CirclesMove makeMoveFromDB(BasicDBObject move) {
		return new CirclesMove(move);
	}

	@Override
	public CirclesGameState makeGameStateFromDB(BasicDBObject dbObject) {
		return new CirclesGameState(dbObject);
	}

	@Override
	public String getRootUrlRoute() {
		return TemplateHandler.CIRCLES_ROUTE;
	}
}
