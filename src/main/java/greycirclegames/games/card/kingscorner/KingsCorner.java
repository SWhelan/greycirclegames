package greycirclegames.games.card.kingscorner;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.games.Game;
import greycirclegames.games.card.Pile;

public class KingsCorner extends Game<KCMove, KCGameState, KCArtificialPlayer>{

	public KingsCorner(int gameId, List<Player> players){
		super(gameId, players);
	}

	//for creating a new kcgame from the mongo object
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
	public boolean endTurn(){
		if(gameIsOver()){
			return false;
		}
		KCGameState gs = getGameState();
		Pile curUserHand = gs.userHands.get(Integer.toString(getCurrentPlayerObject().get_id()));
		Pile drawPile = gs.piles.get(KCPileIds.DRAW_PILE.getKey());
		if(!drawPile.isEmpty()){
			Pile topCard = new Pile("Top card");
			topCard.add(drawPile.getTop());
			KCMove endTurn = new KCMove(getCurrentPlayerObject(), drawPile, topCard, curUserHand);
			endTurn.apply();
			moves.add(endTurn);
		}
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
		
		return true;
	}
	
	@Override
	protected final KCGameState newGameState(List<Player> players) {
		KCGameState game = new KCGameState();
		game.initializeToNewGameState(players);
		return game;
	}
	
	//Checks if any of the player hands are empty.
	@Override
	public boolean gameIsOver(){
		for(Pile p : getGameState().userHands.values()){
			if(p.isEmpty()){
				return true;
			}
		}
		return false;
	}

	//The string identifier for King's Corner
	@Override
	protected String getGameTypeIdentifier() {
		return GlobalConstants.KINGS_CORNER;
	}

	@Override
	protected int determineWinnerId() {
		return players.get(currentPlayerIndex).get_id();
	}

	@Override
	protected KCArtificialPlayer makeArtificialPlayerFromDB(int playerId) {
		return new KCArtificialPlayer(playerId);
	}

	@Override
	protected KCMove makeMoveFromDB(BasicDBObject move) {
		return new KCMove((BasicDBObject)move);
	}

	@Override
	protected KCGameState makeGameStateFromDB(BasicDBObject dbObject) {
		return new KCGameState(dbObject);
	}
}
