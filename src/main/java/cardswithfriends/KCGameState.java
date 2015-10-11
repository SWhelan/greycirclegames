package cardswithfriends;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Authentication.Player;

public class KCGameState extends GameState{
	
	public Map<Integer, Pile> piles;
	
	public Map<Player, Pile> userHands = new HashMap<Player, Pile>();

	public KCGameState(KCGameStateGenerator kc){
		//Initialize to a pre-existing game
	}

	public KCGameState() {
		super();
		//Initialize to a new game
	}
	
	public Pile getNorth() {
		return piles.get(Constants.NORTH_PILE);
	}

	public void setNorth(Pile north) {
		this.north = north;
	}

	public Pile getEast() {
		return east;
	}

	public void setEast(Pile east) {
		this.east = east;
	}

	public Pile getSouth() {
		return south;
	}

	public void setSouth(Pile south) {
		this.south = south;
	}

	public Pile getWest() {
		return west;
	}

	public void setWest(Pile west) {
		this.west = west;
	}

	public Pile getDraw() {
		return draw;
	}

	public void setDraw(Pile draw) {
		this.draw = draw;
	}

	public Pile getNorthEast() {
		return northEast;
	}

	public void setNorthEast(Pile northEast) {
		this.northEast = northEast;
	}

	public Pile getSouthEast() {
		return southEast;
	}

	public void setSouthEast(Pile southEast) {
		this.southEast = southEast;
	}

	public Pile getSouthWest() {
		return southWest;
	}

	public void setSouthWest(Pile southWest) {
		this.southWest = southWest;
	}

	public Pile getNorthWest() {
		return northWest;
	}

	public void setNorthWest(Pile northWest) {
		this.northWest = northWest;
	}

	public Map<Player, Pile> getPlayerHands() {
		return userHands;
	}

	public void setPlayerHands(Map<Player, Pile> userHands) {
		this.userHands = userHands;
	}

	@Override
	protected String toDBForm() {
		// TODO Auto-generated method stub
		return null;
	}

	public static class KCGameStateGenerator{
	}
	
}
