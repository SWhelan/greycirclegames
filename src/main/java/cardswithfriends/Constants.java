package cardswithfriends;

public enum Constants {
	DRAW_PILE (1),
	NORTH_PILE (2),
	EAST_PILE (3),
	SOUTH_PILE (4),
	WEST_PILE (5),
	NORTH_WEST_PILE (6),
	NORTH_EAST_PILE (7),
	SOURTH_EAST_PILE (8),
	SOUTH_WEST_PILE (9);
	
	private int id;
	
	private Constants(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
}
