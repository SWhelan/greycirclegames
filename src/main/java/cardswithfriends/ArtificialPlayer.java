package cardswithfriends;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ArtificialPlayer implements Player {
	private int playerID;

	public ArtificialPlayer(int playerID) {
		super();
		this.playerID = playerID;
	}
	
	public Integer getPlayerID() {
		return playerID;
	}

	public Move createMove(KCGameState gamestate) {
		//Let compatibleCards be all the possible cards that can be used to place a card this turn. (findCompatibleCards)
		Pile compatibleCards = findCompatibleCards(gamestate);
		//Let AIHand be the pile of cards in the AI's hand
		Pile hand = retrieveAIHand(gamestate);
		//Let compatibleHand be the cards in the AI's hand that can be placed(findCompatibleHand)
		Pile compatibleHand = findCompatibleHand(hand, compatibleCards);
		//If compatibleHand is not an empty pile
		if (!compatibleHand.isEmpty())
			//determine the BEST move the AI should make (determineMove) and return it
			return determineMove(gamestate, compatibleHand);
		//otherwise
		else
			//do nothing (end the turn and draw a card)
			return new KCMove(DBHandler.getUser(playerID), null, null, null);
	}

	//perhaps move to KCGameState?
	private Pile retrieveAIHand(KCGameState gamestate) {
		Map<String, Pile> allPiles = gamestate.getPiles();
		return allPiles.get(Integer.toString(playerID));
	}
	//perhaps move to KCGameState?
	private Map<String, Pile> retrieveTablePiles(KCGameState gameState) {
		Map<String, Pile> tablePiles = new HashMap<String, Pile>();
		Map<String, Pile> allPiles = gameState.getPiles();
		tablePiles.put(Integer.toString(PileIds.EAST_PILE.ordinal()), allPiles.get(PileIds.EAST_PILE.ordinal()));
		tablePiles.put(Integer.toString(PileIds.NORTH_PILE.ordinal()), allPiles.get(PileIds.NORTH_PILE.ordinal()));
		tablePiles.put(Integer.toString(PileIds.WEST_PILE.ordinal()), allPiles.get(PileIds.WEST_PILE.ordinal()));
		tablePiles.put(Integer.toString(PileIds.SOUTH_PILE.ordinal()), allPiles.get(PileIds.SOUTH_PILE.ordinal()));
		tablePiles.put(Integer.toString(PileIds.NORTH_EAST_PILE.ordinal()), allPiles.get(PileIds.NORTH_EAST_PILE.ordinal()));
		tablePiles.put(Integer.toString(PileIds.NORTH_WEST_PILE.ordinal()), allPiles.get(PileIds.NORTH_WEST_PILE.ordinal()));
		tablePiles.put(Integer.toString(PileIds.SOUTH_EAST_PILE.ordinal()), allPiles.get(PileIds.SOUTH_EAST_PILE.ordinal()));
		tablePiles.put(Integer.toString(PileIds.SOUTH_WEST_PILE.ordinal()), allPiles.get(PileIds.SOUTH_WEST_PILE.ordinal()));
		return tablePiles;
	}
	private KCMove determineMove(KCGameState gamestate, Pile hand) {
		//Select a random card from the hand
		Pile.shuffle(hand);
		Card moveCard = hand.getTop();
		Pile movingPile = new Pile(moveCard + "");
		movingPile.add(moveCard);
		Pile AIHand = retrieveAIHand(gamestate);
		//Find a table pile in the gamestate to place the card
		Map<String, Pile> tablePiles = retrieveTablePiles(gamestate);
		for (Pile tPile: tablePiles.values()) {
			KCMove possibleMove = new KCMove(DBHandler.getUser(playerID), AIHand, movingPile, tPile);
			if (possibleMove.isValid()) {
				return possibleMove;
			}
		}
		//Create a move with these piles and return
		return new KCMove(DBHandler.getUser(playerID), null, null, null);
	}
	private Pile findCompatibleHand(Pile hand, Pile compatibles) {
		Pile returnPile = new Pile("playableCards");
		//let compatibleHand be the sub-pile of the AI's hand that can be placed this turn
		Card topCard;
		while (!hand.isEmpty()) {
			topCard = hand.removeTop();
			if (compatibles.contains(topCard)) {
				returnPile.add(topCard);
			}
		}
		//for each card in the AI's hand
			//if the compatibles set contains the card
				//Add the AI's card to compatibleHand pile
		//Return the compatibleHand 
		return returnPile;
	}

	private Pile findCompatibleCards(KCGameState gamestate) {
		//Let compCards be the set of compatible cards for the next move
		Map<String, Pile> tablePiles = retrieveTablePiles(gamestate);
		Pile returnPile = new Pile("compatibles");
		for (String p: tablePiles.keySet()) {
			Pile tablePile = tablePiles.get(p);
			Card top;
			try {
				top = tablePile.getTop();
			}
			catch (NoSuchElementException e) {
				top = null;
			}
			Pile compatibles = nextCards(top);
			returnPile.addAll(compatibles);
		}
		//for each directional pile p in gamestate
			//let h be the top card in p
			//Add to compCards the next compatible card with h
		//return compCards
		return returnPile;
	}
	
	private Pile nextCards(Card card) {
		Pile returnPile = new Pile("compatible with " + card);
		if (card == null) {
			returnPile.add(new Card(13, Card.Suit.CLUB));
			returnPile.add(new Card(13, Card.Suit.SPADE));
			returnPile.add(new Card(13, Card.Suit.HEART));
			returnPile.add(new Card(13, Card.Suit.DIAMOND));
		}
		else {
			int cardNumber = card.getNumber();
			if (cardNumber == 1) {
				return returnPile;
			}
			else {
				int compatibleNumber = cardNumber - 1;
				if (card.isRed()) {
					returnPile.add(new Card(compatibleNumber, Card.Suit.CLUB));
					returnPile.add(new Card(compatibleNumber, Card.Suit.SPADE));
				}
				else {
					returnPile.add(new Card(compatibleNumber, Card.Suit.HEART));
					returnPile.add(new Card(compatibleNumber, Card.Suit.DIAMOND));
				}
			}
		}
		return returnPile;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int hashCode(){
		return this.getPlayerID().hashCode();
	}

	@Override
	public Integer get_id() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public String getUserName() {
		return "Computer Player";
	}

	@Override
	public void updateWin(String game) {
		//Do nothing - we do not store stats for ai players currently
	}

	@Override
	public void updateLoss(String game) {
		//Do nothing - we do not store stats for ai players currently
	}
}