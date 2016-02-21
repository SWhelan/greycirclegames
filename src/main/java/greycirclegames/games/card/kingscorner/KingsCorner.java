package greycirclegames.games.card.kingscorner;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import greycirclegames.GlobalConstants;
import greycirclegames.frontend.TemplateHandler;
import greycirclegames.games.Game;
import greycirclegames.games.card.Pile;

public class KingsCorner extends Game<KCMove, KCGameState, KCArtificialPlayer>{

	public KingsCorner(int gameId, List<Integer> players){
		super(gameId, players);
	}

	public KingsCorner(DBObject obj) {
		super.gameFromDBObject(obj);
	}
	
	/**
	 * Ends the turn of the current user.
	 * In King's Corner, a turn end is marked by drawing a card,
	 * so in this method, a card is drawn for the player.
	 * A card does not need to be drawn in addition to calling endTurn.
	 * @return	True if the turn was successfully ended.
	 */
	@Override
	public boolean applyEndTurn(){
		KCGameState gs = getGameState();
		Pile curUserHand = gs.userHands.get(Integer.toString(players.get(currentPlayerIndex)));
		Pile drawPile = gs.piles.get(KCPile.DRAW_PILE.getKey());
		if(!drawPile.isEmpty()){
			Pile topCard = new Pile("Top card");
			topCard.add(drawPile.getTop());
			KCMove endTurn = new KCMove(this.players.get(currentPlayerIndex), drawPile, topCard, curUserHand);
			endTurn.apply();
			moves.add(endTurn);
		}		
		return true;
	}
	
	@Override
	protected final KCGameState newGameState(List<Integer> players) {
		KCGameState state = new KCGameState();
		state.initializeToNewGameState(this, players);
		return state;
	}
	
	@Override
	public boolean gameIsOver(){
		for(Pile p : getGameState().userHands.values()){
			if(p.isEmpty()){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getGameTypeIdentifier() {
		return GlobalConstants.KINGS_CORNER;
	}

	@Override
	protected int determineWinnerId() {
		return players.get(currentPlayerIndex);
	}

	@Override
	public KCArtificialPlayer makeArtificialPlayerFromDB(int playerId) {
		return new KCArtificialPlayer(playerId);
	}

	@Override
	public KCMove makeMoveFromDB(BasicDBObject move) {
		return new KCMove((BasicDBObject)move);
	}

	@Override
	public KCGameState makeGameStateFromDB(BasicDBObject dbObject) {
		return new KCGameState(dbObject);
	}

	@Override
	public String getRootUrlRoute() {
		return TemplateHandler.KINGS_CORNER_ROUTE;
	}
}
