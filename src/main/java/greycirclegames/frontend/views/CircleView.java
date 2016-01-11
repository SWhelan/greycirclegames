package greycirclegames.frontend.views;

public class CircleView {
	public int row;
	public int column;
	public String color;
	public boolean isAPiece = true;
	
	public CircleView(int row, int column, String color, Boolean isAPiece) {
		this.row = row;
		this.column = column;
		this.color = color;
		this.isAPiece = isAPiece;
	}

}
