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
		game.getPlayers().stream().forEach((e) -> { 
			if(e.equals(viewingPlayer)){
				userHand = game.getGameState().userHands.get(viewingPlayer).getCards();
			} else {
				otherPlayers.add(game.getGameState().userHands.get(e).getCards());
			}
		});	
		Map<String, Pile> piles = game.getGameState().piles;
		drawPile = piles.get(PileIds.DRAW_PILE.ordinal()).getCards();
		
		northPile = piles.get(PileIds.NORTH_PILE.ordinal()).getCards();
		eastPile = piles.get(PileIds.EAST_PILE.ordinal()).getCards();
		southPile = piles.get(PileIds.SOUTH_PILE.ordinal()).getCards();
		westPile = piles.get(PileIds.WEST_PILE.ordinal()).getCards();
		
		northEastPile = piles.get(PileIds.NORTH_EAST_PILE.ordinal()).getCards();
		southEastPile = piles.get(PileIds.SOUTH_EAST_PILE.ordinal()).getCards();
		southWestPile = piles.get(PileIds.SOUTH_WEST_PILE.ordinal()).getCards();
		northWestPile = piles.get(PileIds.NORTH_WEST_PILE.ordinal()).getCards();
	}
}
