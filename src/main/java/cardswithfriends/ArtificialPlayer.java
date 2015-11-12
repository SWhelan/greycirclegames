package cardswithfriends;

import java.util.HashMap;
import java.util.Map;

public class ArtificialPlayer implements Player {
	private int playerID;

	public ArtificialPlayer(int playerID) {
		super();
		this.playerID = playerID;
	}
	
	public Integer getPlayerID() {
		return playerID;
	}

	public Move createMove(Pile myHand, Map<Integer,Pile> piles) {
		//Let compatibleCards be all the possible cards that can be used to place a card this turn. (findCompatibleCards)
		//Pile compatibleCards = findCompatibleCards(gamestate);
		//Let AIHand be the pile of cards in the AI's hand
		//Pile hand = retrieveAIHand(gamestate);
		//Let compatibleHand be the cards in the AI's hand that can be placed(findCompatibleHand)
		//Pile compatibleHand = findCompatibleHand(hand, compatibleCards);
		//If compatibleHand is not an empty pile
		//if (!compatibleHand.isEmpty())
			//determine the BEST move the AI should make (determineMove) and return it
		//	return determineMove(gamestate, compatibleHand);
		//otherwise
		//else
			//do nothing (end the turn and draw a card)
		//	return new KCMove(DBHandler.getUser(playerID), null, null, null);
		return null;
	}

	
	private KCMove determineMove(KCGameState gamestate, Pile hand) {
		//TODO: make more intelligent move
		//Select a random card from the hand
		Pile.shuffle(hand);
		Card moveCard = hand.getTop();
		//Find a table pile in the gamestate to place the card

		//Create a move with these piles and return
		return new KCMove(DBHandler.getUser(playerID), null, null, null);
	}
	private Pile findCompatibleHand(Pile hand, Pile compatibles) {
		//let compatibleHand be the sub-pile of the AI's hand that can be placed this turn
		//for each card in the AI's hand
			//if the compatibles set contains the card
				//Add the AI's card to compatibleHand pile
		//Return the compatibleHand 
		return null;
	}

	private Pile findCompatibleCards(KCGameState gamestate) {
		//Let compCards be the set of compatible cards for the next move
		//Map<String, Pile> tablePiles = retrieveTablePiles(gamestate);

		//for each directional pile p in gamestate
			//let h be the top card in p
			//Add to compCards the next compatible card with h
		//return compCards
		return null;
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