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
		
		Pile drawPile = piles.get(Constants.DRAW_PILE);
		
		//Deal cards to users
		for(int i = 0; i < 7; i++){
			for(Pile p : userHands.values()){
				p.add(drawPile.removeTop());
			}
		}
		
		//Add cards to initial piles
		addCardTo(piles.get(Constants.NORTH_PILE), piles.get(Constants.NORTH_WEST_PILE), drawPile);
		addCardTo(piles.get(Constants.EAST_PILE), piles.get(Constants.NORTH_EAST_PILE), drawPile);
		addCardTo(piles.get(Constants.SOUTH_PILE), piles.get(Constants.SOUTH_WEST_PILE), drawPile);
		addCardTo(piles.get(Constants.WEST_PILE), piles.get(Constants.SOUTH_EAST_PILE), drawPile);
	}

	private void addCardTo(Pile notKing, Pile isKing, Pile drawPile) {
		Card c = drawPile.removeTop();
		//If the card is a king
		if(c.getNumber() != 12){
			notKing.add(c);
		}else{
			isKing.add(c);
		}
	}

	private void initializePiles() {
		piles = new HashMap<Integer, Pile>();
		Pile drawPile = Pile.makeDeck("Draw Pile");
		Pile.shuffle(drawPile);
		piles.put(Constants.DRAW_PILE.getId(), drawPile);
		piles.put(Constants.EAST_PILE.getId(), new Pile("East Pile"));
		piles.put(Constants.NORTH_PILE.getId(), new Pile("North Pile"));
		piles.put(Constants.SOUTH_PILE.getId(), new Pile("South Pile"));
		piles.put(Constants.WEST_PILE.getId(), new Pile("West Pile"));
		piles.put(Constants.NORTH_EAST_PILE.getId(), new Pile("Northeast Pile"));
		piles.put(Constants.NORTH_WEST_PILE.getId(), new Pile("Northwest Pile"));
		piles.put(Constants.SOUTH_EAST_PILE.getId(), new Pile("Southeast Pile"));
		piles.put(Constants.SOUTH_WEST_PILE.getId(), new Pile("Southwest Pile"));
	}
	
//	public static KingsCorner updateKCGame(int gameID, GameState gameState) {
//		return DBHandler.updateKCGame(gameID, gameState);
//	}
	
}
