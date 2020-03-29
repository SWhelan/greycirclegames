package greycirclegames.frontend.views;

public class CircleView {
	public int row;
	public int column;
	public String color;
	public boolean isAPiece = true;
	public boolean isAPossibleMove = false;
	public boolean isRelevantMove = false;
	
	public CircleView(int row, int column, String color, boolean isAPiece, boolean isRelevantMove) {
		this.row = row;
		this.column = column;
		this.color = color;
		this.isAPiece = isAPiece;
		this.isRelevantMove = isRelevantMove;
	}
	
   public CircleView(int row, int column, boolean isAPossibleMove) {
       this(row, column, "", false, false);
        this.isAPossibleMove = isAPossibleMove;
    }

}
