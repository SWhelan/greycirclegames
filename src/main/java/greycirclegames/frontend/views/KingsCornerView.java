package greycirclegames.frontend.views;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import greycirclegames.DBHandler;
import greycirclegames.Player;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.Pile;
import greycirclegames.games.card.kingscorner.KCPile;
import greycirclegames.games.card.kingscorner.KingsCorner;

public class KingsCornerView {
	public Integer gameId;
	public String gameRoute;
	public String gameTypeId;
	public boolean isTurn;
	public boolean isActive;
	public List<CardView> userHand;
	public List<HandView> otherPlayers = new LinkedList<HandView>();
	public List<CardView> drawPile;
	
	public List<CardView> northPile;
	public List<CardView> eastPile;
	public List<CardView> southPile;
	public List<CardView> westPile;
	
	public List<CardView> northEastPile;
	public List<CardView> southEastPile;
	public List<CardView> southWestPile;
	public List<CardView> northWestPile;
	
	public List<String> moveHistory;
	public boolean isWinner;
	public boolean isTie;
	
	public KingsCornerView(KingsCorner game, Player viewingPlayer){
		gameId = game.get_id();
		int currentPlayerId = game.getPlayers().get(game.getCurrentPlayerIndex());
		game.getPlayers().stream().forEach((e) -> { 
			Player player = null;
			if(e < 0){
				player = game.makeArtificialPlayer(e);
			} else {
				player = DBHandler.getUser(e);
			}
			if(player.get_id().equals(viewingPlayer.get_id())){
				userHand = makeCardView(game.getGameState().userHands.get(Integer.toString(viewingPlayer.get_id())).getCards());
			} else {
				otherPlayers.add(new HandView(makeCardView(game.getGameState().userHands.get(Integer.toString(player.get_id())).getCards()), player.getUsername(), player.get_id() == currentPlayerId));
			}
		});	
		Map<String, Pile> piles = game.getGameState().piles;
		drawPile = makeCardView(piles.get(KCPile.DRAW_PILE.getKey()).getCards());
		
		northPile = makeCardView(piles.get(KCPile.NORTH_PILE.getKey()).getCards());
		eastPile = makeCardView(piles.get(KCPile.EAST_PILE.getKey()).getCards());
		southPile = makeCardView(piles.get(KCPile.SOUTH_PILE.getKey()).getCards());
		westPile = makeCardView(piles.get(KCPile.WEST_PILE.getKey()).getCards());
		
		northEastPile = makeCardView(piles.get(KCPile.NORTH_EAST_PILE.getKey()).getCards());
		southEastPile = makeCardView(piles.get(KCPile.SOUTH_EAST_PILE.getKey()).getCards());
		southWestPile = makeCardView(piles.get(KCPile.SOUTH_WEST_PILE.getKey()).getCards());
		northWestPile = makeCardView(piles.get(KCPile.NORTH_WEST_PILE.getKey()).getCards());
		
		northPile = removeMiddle(northPile);
		eastPile = removeMiddle(eastPile);
		southPile = removeMiddle(southPile);
		westPile = removeMiddle(westPile);
		northEastPile = removeMiddle(northEastPile);
		southEastPile = removeMiddle(southEastPile);
		southWestPile = removeMiddle(southWestPile);
		northWestPile = removeMiddle(northWestPile);
		int currentPlayerIdCheck = currentPlayerId;
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
		isTie = game.getTie();
		this.gameRoute = game.getRootUrlRoute();
		this.gameTypeId = game.getGameTypeIdentifier();
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