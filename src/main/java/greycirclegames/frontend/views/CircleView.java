package greycirclegames.frontend.views;

public class CircleView {
	public int row;
	public int column;
	public String hex;
	public boolean isAPiece = true;
	
	public CircleView(int row, int column, String hex, Boolean isAPiece) {
		this.row = row;
		this.column =column;
		this.hex = hex;
		this.isAPiece = isAPiece;
	}

}
