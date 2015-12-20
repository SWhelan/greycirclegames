package greycirclegames.games.card.kingscorner;

import java.util.List;
import java.util.Map;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;
import greycirclegames.games.card.CardBasedMove;
import greycirclegames.games.card.Pile;

public class KingsCorner extends Game{

	public KingsCorner(int gameId, List<Player> players){
		super(gameId, players);
	}

	//for creating a new kcgame from the mongo object
	public KingsCorner(DBObject obj) {
		super.gameFromDBObject(obj);
		this.gameState = new KCGameState((BasicDBObject)obj.get("GameState"));
	}
	
	/**
	 * Ends the turn of the current user.
	 * In King's Corner, a turn end is marked by drawing a card,
	 * so in this method, a card is drawn for the player.
	 * A card does not need to be drawn in addition to calling endTurn.
	 * @return	True if the tun was successfully ended.
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
			CardBasedMove endTurn = new KCMove(getCurrentPlayerObject(), drawPile, topCard, curUserHand);
			endTurn.apply();
			moves.add(endTurn);
		}
		currentPlayer = (currentPlayer + 1) % turnOrder.size();
		
		return true;
	}
	
	/**
	 * If the current move is an AI, we can call this method to play the AI's moves.
	 * Also, if the next player(s) is also an AI, this method will play all those
	 * AI's moves.
	 * @return	True if any AI moves were made.
	 */
	public boolean applyAIMoves(){
		boolean result = false;
		Player cur = getCurrentPlayerObject();
		Pile aiHand;
		Map<Integer,Pile> visiblePiles = getGameState().getVisiblePiles();
		CardBasedMove m = null;
		//While the game is still active and the current player is an AI
		while(this.isActive && isAI(cur)){
			result = true;
			aiHand = getGameState().userHands.get(Integer.toString(cur.get_id()));

			ArtificialPlayer ai = (ArtificialPlayer) cur;
			boolean hasMove = true;
			//Get as many moves from the player as we can (until null)
			while(this.isActive && hasMove){
				m = ai.createMove(aiHand, visiblePiles);
				if(m != null){
					this.applyMove(m);
				}else{
					hasMove = false;
				}				
			}
			//End the turn
			this.endTurn();
			//Get next player
			cur = getCurrentPlayerObject();
		}
		return result;
	}
	
	/**
	 * Returns the GameState as a KCGameState object.
	 */
	public KCGameState getGameState(){
		return (KCGameState) super.getGameState();
	}
	
	@Override
	protected final GameState newGameState(List<Player> players) {
		GameState game = new KCGameState();
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
}
