package greycirclegames.frontend.views;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import greycirclegames.Player;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.Pile;
import greycirclegames.games.card.kingscorner.KCPileIds;
import greycirclegames.games.card.kingscorner.KingsCorner;

public class KingsCornerView {
	Integer gameId;
	boolean isTurn;
	boolean isActive;
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
	boolean isWinner;
	
	public KingsCornerView(KingsCorner game, Player viewingPlayer){
		gameId = game.get_id();
		int currentPlayerId = game.getCurrentPlayerObject().get_id();
		game.getPlayers().stream().forEach((e) -> { 
			if(e.equals(viewingPlayer)){
				userHand = makeCardView(game.getGameState().userHands.get(Integer.toString(viewingPlayer.get_id())).getCards());
			} else {
				otherPlayers.add(new HandView(makeCardView(game.getGameState().userHands.get(Integer.toString(e.get_id())).getCards()), e.getUserName(), e.get_id() == currentPlayerId));
			}
		});	
		Map<String, Pile> piles = game.getGameState().piles;
		drawPile = makeCardView(piles.get(KCPileIds.DRAW_PILE.getKey()).getCards());
		
		northPile = makeCardView(piles.get(KCPileIds.NORTH_PILE.getKey()).getCards());
		eastPile = makeCardView(piles.get(KCPileIds.EAST_PILE.getKey()).getCards());
		southPile = makeCardView(piles.get(KCPileIds.SOUTH_PILE.getKey()).getCards());
		westPile = makeCardView(piles.get(KCPileIds.WEST_PILE.getKey()).getCards());
		
		northEastPile = makeCardView(piles.get(KCPileIds.NORTH_EAST_PILE.getKey()).getCards());
		southEastPile = makeCardView(piles.get(KCPileIds.SOUTH_EAST_PILE.getKey()).getCards());
		southWestPile = makeCardView(piles.get(KCPileIds.SOUTH_WEST_PILE.getKey()).getCards());
		northWestPile = makeCardView(piles.get(KCPileIds.NORTH_WEST_PILE.getKey()).getCards());
		
		northPile = removeMiddle(northPile);
		eastPile = removeMiddle(eastPile);
		southPile = removeMiddle(southPile);
		westPile = removeMiddle(westPile);
		northEastPile = removeMiddle(northEastPile);
		southEastPile = removeMiddle(southEastPile);
		southWestPile = removeMiddle(southWestPile);
		northWestPile = removeMiddle(northWestPile);
		int currentPlayerIdCheck = game.getCurrentPlayerObject().get_id();
		int viewingPlayerIdCheck = viewingPlayer.get_id();
		isTurn = currentPlayerIdCheck == viewingPlayerIdCheck; 
		
		this.moveHistory = game.getMoves().stream().map(e -> e.toString()).collect(Collectors.toList());
		Collections.reverse(this.moveHistory);
		
		isActive = game.getIsActive();
		if(!isActive){
			if(((int)game.getWinner_id()) == viewingPlayer.get_id()){
				isWinner = true;
			}
		}
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