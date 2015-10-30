package cardswithfriends;
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
		Set<Card> compatibleCards = findCompatibleCards(gamestate);
		//Let AIHand be the pile of cards in the AI's hand
		Pile hand = retrieveAIHand(gamestate);
		//Let compatibleHand be the cards in the AI's hand that can be placed(findCompatibleHand)
		Set<Card> compatibleHand = findCompatibleHand(hand, compatibleCards);
		//If compatibleHand is not an empty pile
		if (!compatibleHand.isEmpty())
			//determine the BEST move the AI should make (determineMove) and return it
			return determineMove(gamestate, compatibleHand);
		//otherwise
		else
			//do nothing (end the turn and draw a card)
			return new KCMove(playerID, null, null, null);
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
		tablePiles.put(PileIds.EAST_PILE.ordinal(), allPiles.get(PileIds.EAST_PILE.ordinal()));
		tablePiles.put(PileIds.NORTH_PILE.ordinal(), allPiles.get(PileIds.NORTH_PILE.ordinal()));
		tablePiles.put(PileIds.WEST_PILE.ordinal(), allPiles.get(PileIds.WEST_PILE.ordinal()));
		tablePiles.put(PileIds.SOUTH_PILE.ordinal(), allPiles.get(PileIds.SOUTH_PILE.ordinal()));
		tablePiles.put(PileIds.NORTH_EAST_PILE.ordinal(), allPiles.get(PileIds.NORTH_EAST_PILE.ordinal()));
		tablePiles.put(PileIds.NORTH_WEST_PILE.ordinal(), allPiles.get(PileIds.NORTH_WEST_PILE.ordinal()));
		tablePiles.put(PileIds.SOUTH_EAST_PILE.ordinal(), allPiles.get(PileIds.SOUTH_EAST_PILE.ordinal()));
		tablePiles.put(PileIds.SOUTH_WEST_PILE.ordinal(), allPiles.get(PileIds.SOUTH_WEST_PILE.ordinal()));
		return tablePiles;
	}
	private KOMove determineMove(KCGameState gamestate, Pile hand) {
		//TODO: make more intelligent move
		//Select a random card from the hand
		Pile.shuffle(hand);
		Card moveCard = hand.getTop();
		//Find a table pile in the gamestate to place the card

		//Create a move with these piles and return
		return new KOMove(playerID, null, null, null);
	}
	private boolean findCompatibleHand(Pile hand, Set<Card> compatibles) {
		//let compatibleHand be the sub-pile of the AI's hand that can be placed this turn
		//for each card in the AI's hand
			//if the compatibles set contains the card
				//Add the AI's card to compatibleHand pile
		//Return the compatibleHand 
		return false;
	}

	private Set<Card> findCompatibleCards(KCGameState gamestate) {
		//Let compCards be the set of compatible cards for the next move
		Map<String, Pile> tablePiles = retrieveTablePiles(gamestate);

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
}