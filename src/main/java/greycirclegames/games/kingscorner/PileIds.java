package greycirclegames.games.kingscorner;

public enum PileIds {
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
