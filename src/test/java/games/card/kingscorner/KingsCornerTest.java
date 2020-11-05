package games.card.kingscorner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import greycirclegames.DatabaseConnector;
import greycirclegames.Player;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.Pile;
import greycirclegames.games.card.kingscorner.KCGameState;
import greycirclegames.games.card.kingscorner.KCMove;
import greycirclegames.games.card.kingscorner.KCPile;
import greycirclegames.games.card.kingscorner.KingsCorner;
import spark.utils.Assert;

public class KingsCornerTest {
	private static final List<Integer> players = new ArrayList<Integer>(Arrays.asList(-1, -2, -3));
	
    public void testInit() {
    	DatabaseConnector.getInstance().setTestDatabase();
    }
	
	public void testKingsCornerEndTurn(){
    	KingsCorner kc = new KingsCorner(0, players);
    	KCGameState gs = (KCGameState) kc.getGameState();
    	
    	Pile drawPile = gs.piles.get(KCPile.DRAW_PILE.getKey());
    	Card topCard = drawPile.getTop();
    	
    	Player currentPlayer = kc.getCurrentPlayerObject();
    	Pile currentPlayerHand = gs.userHands.get(Integer.toString(currentPlayer.get_id()));
    	Assert.isTrue(!currentPlayerHand.contains(topCard), "The card in the draw pile should not be in a player's hand.");
    	kc.endTurn();
    	Assert.isTrue(currentPlayerHand.size() == 8, "Ending a turn should draw a card.");
    	Assert.isTrue(currentPlayerHand.contains(topCard), "The top card in the draw pile should be drawn.");
    	Assert.isTrue(!currentPlayer.equals(kc.getCurrentPlayerObject()), "Current player should have been incremented.");
    }
    
    public void testKingsCornerApplyMove(){
    	KingsCorner kc = new KingsCorner(0, players);
    	KCGameState gs = (KCGameState) kc.getGameState();
    	
    	Pile spoof = new Pile("Spoof North Pile");
    	spoof.addOne(Card.make(8, Card.Suit.CLUB));
    	gs.piles.put(KCPile.NORTH_PILE.getKey(), spoof);
    	Pile user0Hand = gs.userHands.get(Integer.toString(players.get(0)));
    	Card toMove = Card.make(7, Card.Suit.DIAMOND);
    	if(!user0Hand.contains(toMove)){
    		user0Hand.add(toMove);
    	}
    	
    	Pile moving = new Pile("Moving Pile");
    	moving.add(toMove);
    	
    	KCMove move = new KCMove(players.get(0), user0Hand, moving, spoof);
    	
    	Assert.isTrue(move.isValid(), "This should be a valid move.");
    	Assert.isTrue(kc.applyMove(move), "This is a valid move and the game should not be over, so return should be true.");
    	
    	Assert.isTrue(!user0Hand.contains(toMove), "Card should not be in user hand.");
    	Assert.isTrue(!user0Hand.containsAll(moving), "Card should not be in user hand.");
    	
    	Pile expected = new Pile("Expected pile.");
    	expected.addOne(Card.make(8, Card.Suit.CLUB));
    	expected.addOne(toMove);
    	Assert.isTrue(expected.equals(spoof), "We expect this pile.");
    	List<KCMove> moves = kc.getMoves();
    	KCMove mostRecent = moves.get(moves.size()-1);
    	
    	Assert.isTrue(mostRecent.getOriginName().equals(user0Hand.getName()), "Most recent origin is user 0 hand.");
    	Assert.isTrue(mostRecent.getMovingName().equals("Moving Pile"), "Most recent moving pile.");
    	Assert.isTrue(mostRecent.getDestinationName().equals(spoof.getName()), "Most recent destination");
    }
    
    public void testKingsCornerApplyMoveInvalidMove(){
    	KingsCorner kc = new KingsCorner(0, players);
    	KCGameState gs = (KCGameState) kc.getGameState();
    	
    	Pile spoof = new Pile("Spoof North Pile");
    	spoof.addOne(Card.make(10, Card.Suit.CLUB));
    	gs.piles.put(KCPile.NORTH_PILE.getKey(), spoof);
    	Pile user0Hand = gs.userHands.get(Integer.toString(players.get(0)));
    	Card toMove = Card.make(7, Card.Suit.DIAMOND);
    	if(!user0Hand.contains(toMove)){
    		user0Hand.add(toMove);
    	}
    	
    	Pile moving = new Pile("Moving Pile");
    	moving.add(toMove);
    	
    	KCMove move = new KCMove(players.get(0), user0Hand, moving, spoof);
    	
    	Assert.isTrue(!move.isValid(), "This should be an invalid move.");
    	Assert.isTrue(!kc.applyMove(move), "This is not a valid move, so return should be false.");
    }
    
    public void testKingsCornerGameOverConditions(){
    	KingsCorner kc = new KingsCorner(0, players);
    	KCGameState gs = kc.getGameState();

    	Assert.isTrue(!kc.gameIsOver(), "The intial game should not be over");
    	Assert.isTrue(kc.getWinner() == null, "There is no winner.");
    	
    	gs.userHands.put(Integer.toString(players.get(0)), new Pile("Empty Pile"));
    	
    	Assert.isTrue(kc.gameIsOver(), "The game should be over");
    }
}
