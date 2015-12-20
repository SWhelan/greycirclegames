package greycirclegames.games.card.kingscorner;

public enum KCPileIds {
	DRAW_PILE,
	NORTH_PILE,
	EAST_PILE,
	SOUTH_PILE,
	WEST_PILE,
	NORTH_WEST_PILE,
	NORTH_EAST_PILE,
	SOUTH_EAST_PILE,
	SOUTH_WEST_PILE;
	
	public String getKey(){
		return Integer.toString(this.ordinal());
	}
}
