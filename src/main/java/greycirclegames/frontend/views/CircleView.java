package greycirclegames.frontend.views;

public class CircleView {
	public int row;
	public int column;
	public String color;
	public boolean isAPiece = true;
	public boolean isAPossibleMove = false;
	
	public CircleView(int row, int column, String color, Boolean isAPiece) {
		this.row = row;
		this.column = column;
		this.color = color;
		this.isAPiece = isAPiece;
	}
	
   public CircleView(int row, int column, Boolean isAPossibleMove) {
       this(row, column, "", false);
        this.isAPossibleMove = isAPossibleMove;
    }

}
