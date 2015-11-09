package cardswithfriends.views;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cardswithfriends.Card;
import cardswithfriends.KingsCorner;
import cardswithfriends.Pile;
import cardswithfriends.PileIds;
import cardswithfriends.Player;

public class GameView {
	Integer gameId;
	boolean isTurn;
	List<CardView> userHand;
	List<List<CardView>> otherPlayers = new LinkedList<List<CardView>>();
	List<CardView> drawPile;
	
	List<CardView> northPile;
	List<CardView> eastPile;
	List<CardView> southPile;
	List<CardView> westPile;
	
	List<CardView> northEastPile;
	List<CardView> southEastPile;
	List<CardView> southWestPile;
	List<CardView> northWestPile;
	
	public GameView(KingsCorner game, Player viewingPlayer){
		gameId = game.get_id();
		game.getPlayers().stream().forEach((e) -> { 
			if(e.equals(viewingPlayer)){
				userHand = makeCardView(game.getGameState().userHands.get(Integer.toString(viewingPlayer.get_id())).getCards());
			} else {
				otherPlayers.add(makeCardView(game.getGameState().userHands.get(Integer.toString(e.get_id())).getCards()));
			}
		});	
		Map<String, Pile> piles = game.getGameState().piles;
		drawPile = makeCardView(piles.get(Integer.toString(PileIds.DRAW_PILE.ordinal())).getCards());
		
		northPile = makeCardView(piles.get(Integer.toString(PileIds.NORTH_PILE.ordinal())).getCards());
		eastPile = makeCardView(piles.get(Integer.toString(PileIds.EAST_PILE.ordinal())).getCards());
		southPile = makeCardView(piles.get(Integer.toString(PileIds.SOUTH_PILE.ordinal())).getCards());
		westPile = makeCardView(piles.get(Integer.toString(PileIds.WEST_PILE.ordinal())).getCards());
		
		northEastPile = makeCardView(piles.get(Integer.toString(PileIds.NORTH_EAST_PILE.ordinal())).getCards());
		southEastPile = makeCardView(piles.get(Integer.toString(PileIds.SOUTH_EAST_PILE.ordinal())).getCards());
		southWestPile = makeCardView(piles.get(Integer.toString(PileIds.SOUTH_WEST_PILE.ordinal())).getCards());
		northWestPile = makeCardView(piles.get(Integer.toString(PileIds.NORTH_WEST_PILE.ordinal())).getCards());
		
		removeMiddle(northPile);
		removeMiddle(eastPile);
		removeMiddle(southPile);
		removeMiddle(westPile);
		removeMiddle(northEastPile);
		removeMiddle(southEastPile);
		removeMiddle(southWestPile);
		removeMiddle(northWestPile);
		isTurn = game.getCurrentPlayerObject().get_id() == viewingPlayer.get_id();
	}
	
	private List<CardView> removeMiddle(List<CardView> pile){
		for(int i = pile.size()-2; i > 0; i--){
			pile.remove(i);
		}
		if(pile.size() > 1){
			pile.get(0).setFirst(true);
		}
		return pile;
	}
	
	private List<CardView> makeCardView(List<Card> cards){
		return cards.stream().map(e -> new CardView(e)).collect(Collectors.toList());
	}
}