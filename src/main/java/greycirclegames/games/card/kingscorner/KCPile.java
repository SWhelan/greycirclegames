package greycirclegames.games.card.kingscorner;

public enum KCPile {
	DRAW_PILE("Draw Pile"),
	NORTH_PILE("North Pile"),
	EAST_PILE("East Pile"),
	SOUTH_PILE("South Pile"),
	WEST_PILE("West Pile"),
	NORTH_WEST_PILE("Northwest Pile"),
	NORTH_EAST_PILE("Northeast Pile"),
	SOUTH_EAST_PILE("Southeast Pile"),
	SOUTH_WEST_PILE("Southwest Pile");
	
	private String prettyName;
	
	private KCPile(String prettyName){
		this.prettyName = prettyName;
	}
	
	public String getKey(){
		return Integer.toString(this.ordinal());
	}
	
	public String getPrettyName(){
		return prettyName;
	}
}
