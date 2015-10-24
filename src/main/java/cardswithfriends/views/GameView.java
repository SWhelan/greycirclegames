package cardswithfriends.views;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cardswithfriends.Card;
import cardswithfriends.KingsCorner;
import cardswithfriends.Pile;
import cardswithfriends.Player;

public class GameView {
	List<Card> userHand = new LinkedList<Card>();
	List<Integer> numCards = new LinkedList<Integer>();
	List<Card> drawPile;
	public GameView(KingsCorner game, Player viewingPlayer){
		game.getPlayers().stream().forEach((e) -> { 
			if(e.equals(viewingPlayer)){
				Pile viewingPile = game.getGameState().userHands.get(viewingPlayer);
				for(int i = 0; i < viewingPile.size(); i++){
					userHand.add(viewingPile.get(i));
				}
			} else {
				numCards.add(game.getGameState().userHands.get(e).size());
			}
		});	
		Map<Integer, Pile> piles = game.getGameState().piles;
		//drawPile = piles.get(PileIds.DRAW_PILE);
	}
}
