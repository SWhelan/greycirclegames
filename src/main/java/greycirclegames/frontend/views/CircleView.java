package greycirclegames.frontend.views;

public class CircleView {
	public int row;
	public int column;
	public String hex;
	public boolean isAPiece = true;
	
	public CircleView(int i, int j, String hex, Boolean isAPiece) {
		this.row = i;
		this.column = j;
		this.hex = hex;
		this.isAPiece = isAPiece;
	}

}
