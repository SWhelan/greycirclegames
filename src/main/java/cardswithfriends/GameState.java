package cardswithfriends;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Authentication.User;

public class GameState {
	private int turnNumber;
	private int userId;
	private Pile north;
	private Pile east;
	private Pile south;
	private Pile west;
	private Pile draw;
	private Pile northEast;
	private Pile southEast;
	private Pile southWest;
	private Pile northWest;
	
	private Map<User, Pile> userHands = new HashMap<User, Pile>();

	public int getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Pile getNorth() {
		return north;
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

	public Map<User, Pile> getUserHands() {
		return userHands;
	}

	public void setUserHands(Map<User, Pile> userHands) {
		this.userHands = userHands;
	}
	
	
}
