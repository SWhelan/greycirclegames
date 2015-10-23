package cardswithfriends;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KCGameState extends GameState{
	
	public Map<Integer, Pile> piles;
	
	public Map<Player, Pile> userHands = new HashMap<Player, Pile>();

	public KCGameState(KCGameStateGenerator kc){
		//Initialize to a pre-existing game
	}

	//Initialize to new gamestate
	public KCGameState() {
		//Initialize to a new game
		super();
	}

	public Map<Player, Pile> getPlayerHands() {
		return userHands;
	}

	public void setPlayerHands(Map<Player, Pile> userHands) {
		this.userHands = userHands;
	}

	public static class KCGameStateGenerator{
	}
	
	public static KingsCorner getKCGame(int gameID) {
		return DBHandler.getKCGame(gameID);
	}

	@Override
	protected void initializeToNewGameState(List<Player> players) {
		initializePiles();
		userHands = new HashMap<Player, Pile>();
		for(Player p : players){
			userHands.put(p, new Pile(p.getUserName()+"'s Pile"));
		}
		
		Pile drawPile = piles.get(PileIds.DRAW_PILE.getId());
		
		//Deal cards to users
		for(int i = 0; i < 7; i++){
			for(Pile p : userHands.values()){
				p.add(drawPile.removeTop());
			}
		}
		
		//Add cards to initial piles
		addCardTo(piles.get(PileIds.NORTH_PILE.getId()), piles.get(PileIds.NORTH_WEST_PILE.getId()), drawPile);
		addCardTo(piles.get(PileIds.EAST_PILE.getId()), piles.get(PileIds.NORTH_EAST_PILE.getId()), drawPile);
		addCardTo(piles.get(PileIds.SOUTH_PILE.getId()), piles.get(PileIds.SOUTH_WEST_PILE.getId()), drawPile);
		addCardTo(piles.get(PileIds.WEST_PILE.getId()), piles.get(PileIds.SOUTH_EAST_PILE.getId()), drawPile);
	}

	private void addCardTo(Pile notKing, Pile isKing, Pile drawPile) {
		Card c = drawPile.removeTop();
		//If the card is a king
		if(c.getNumber() != GlobalConstants.KING){
			notKing.add(c);
		}else{
			isKing.add(c);
		}
	}

	private void initializePiles() {
		piles = new HashMap<Integer, Pile>();
		Pile drawPile = Pile.makeDeck("Draw Pile");
		Pile.shuffle(drawPile);
		piles.put(PileIds.DRAW_PILE.getId(), drawPile);
		piles.put(PileIds.EAST_PILE.getId(), new Pile("East Pile"));
		piles.put(PileIds.NORTH_PILE.getId(), new Pile("North Pile"));
		piles.put(PileIds.SOUTH_PILE.getId(), new Pile("South Pile"));
		piles.put(PileIds.WEST_PILE.getId(), new Pile("West Pile"));
		piles.put(PileIds.NORTH_EAST_PILE.getId(), new Pile("Northeast Pile"));
		piles.put(PileIds.NORTH_WEST_PILE.getId(), new Pile("Northwest Pile"));
		piles.put(PileIds.SOUTH_EAST_PILE.getId(), new Pile("Southeast Pile"));
		piles.put(PileIds.SOUTH_WEST_PILE.getId(), new Pile("Southwest Pile"));
	}
	
}
