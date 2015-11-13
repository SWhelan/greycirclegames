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
	List<HandView> otherPlayers = new LinkedList<HandView>();
	List<CardView> drawPile;
	
	List<CardView> northPile;
	List<CardView> eastPile;
	List<CardView> southPile;
	List<CardView> westPile;
	
	List<CardView> northEastPile;
	List<CardView> southEastPile;
	List<CardView> southWestPile;
	List<CardView> northWestPile;
	
	List<String> moveHistory;
	
	public GameView(KingsCorner game, Player viewingPlayer){
		gameId = game.get_id();
		int currentPlayerId = game.getCurrentPlayer();
		game.getPlayers().stream().forEach((e) -> { 
			if(e.equals(viewingPlayer)){
				userHand = makeCardView(game.getGameState().userHands.get(Integer.toString(viewingPlayer.get_id())).getCards());
			} else {
				otherPlayers.add(new HandView(makeCardView(game.getGameState().userHands.get(Integer.toString(e.get_id())).getCards()), e.getUserName(), e.get_id() == currentPlayerId));
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
		
		northPile = removeMiddle(northPile);
		eastPile = removeMiddle(eastPile);
		southPile = removeMiddle(southPile);
		westPile = removeMiddle(westPile);
		northEastPile = removeMiddle(northEastPile);
		southEastPile = removeMiddle(southEastPile);
		southWestPile = removeMiddle(southWestPile);
		northWestPile = removeMiddle(northWestPile);
		isTurn = game.getCurrentPlayerObject().get_id() == viewingPlayer.get_id();
		
		this.moveHistory = game.getMoves().stream().map(e -> e.toString()).collect(Collectors.toList());
	}
	
	private List<CardView> removeMiddle(List<CardView> pile){
		List<CardView> newPile = new LinkedList<CardView>();
		if(pile.size() > 0){
			newPile.add(pile.get(0));
		}
		if(pile.size() > 1){
			newPile.add(pile.get(pile.size()-1));
			newPile.get(0).setFirst(true);
		}
		return newPile;
	}
	
	private List<CardView> makeCardView(List<Card> cards){
		return cards.stream().map(e -> new CardView(e)).collect(Collectors.toList());
	}
}