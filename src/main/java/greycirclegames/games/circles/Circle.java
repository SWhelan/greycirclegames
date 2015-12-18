package greycirclegames.games.circles;

public enum Circle {
	WHITE("White", "#ffffff"),
	BLACK("Black", "#000000");
	
	private String name;
	private String hex;
	
	private Circle(String name, String hex){
		this.name = name;
		this.hex = hex;
	}
	
	public String getName(){
		return name;
	}
	
	public String getHex(){
		return hex;
	}
}
