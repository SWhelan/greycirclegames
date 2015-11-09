package cardswithfriends.views;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cardswithfriends.Card;
import cardswithfriends.KingsCorner;
import cardswithfriends.Pile;
import cardswithfriends.PileIds;
import cardswithfriends.Player;

public class GameView {
	Integer gameId;
	boolean isTurn;
	List<Card> userHand;
	List<List<Card>> otherPlayers = new LinkedList<List<Card>>();
	List<Card> drawPile;
	
	List<Card> northPile;
	List<Card> eastPile;
	List<Card> southPile;
	List<Card> westPile;
	
	List<Card> northEastPile;
	List<Card> southEastPile;
	List<Card> southWestPile;
	List<Card> northWestPile;
	
	public GameView(KingsCorner game, Player viewingPlayer){
		gameId = game.get_id();
		game.getPlayers().stream().forEach((e) -> { 
			if(e.equals(viewingPlayer)){
				userHand = game.getGameState().userHands.get(Integer.toString(viewingPlayer.get_id())).getCards();
			} else {
				otherPlayers.add(game.getGameState().userHands.get(Integer.toString(e.get_id())).getCards());
			}
		});	
		Map<String, Pile> piles = game.getGameState().piles;
		drawPile = piles.get(Integer.toString(PileIds.DRAW_PILE.ordinal())).getCards();
		
		northPile = piles.get(Integer.toString(PileIds.NORTH_PILE.ordinal())).getCards();
		eastPile = piles.get(Integer.toString(PileIds.EAST_PILE.ordinal())).getCards();
		southPile = piles.get(Integer.toString(PileIds.SOUTH_PILE.ordinal())).getCards();
		westPile = piles.get(Integer.toString(PileIds.WEST_PILE.ordinal())).getCards();
		
		northEastPile = piles.get(Integer.toString(PileIds.NORTH_EAST_PILE.ordinal())).getCards();
		southEastPile = piles.get(Integer.toString(PileIds.SOUTH_EAST_PILE.ordinal())).getCards();
		southWestPile = piles.get(Integer.toString(PileIds.SOUTH_WEST_PILE.ordinal())).getCards();
		northWestPile = piles.get(Integer.toString(PileIds.NORTH_WEST_PILE.ordinal())).getCards();
		
		removeMiddle(northPile);
		removeMiddle(eastPile);
		removeMiddle(southPile);
		removeMiddle(westPile);
		removeMiddle(northEastPile);
		removeMiddle(southEastPile);
		removeMiddle(southWestPile);
		removeMiddle(northWestPile);
		isTurn = game.getCurrentPlayer() == viewingPlayer.get_id();
	}
	
	private List<Card> removeMiddle(List<Card> pile){
		for(int i = pile.size()-2; i > 1; i--){
			pile.remove(i);
		}
		return pile;
	}

}