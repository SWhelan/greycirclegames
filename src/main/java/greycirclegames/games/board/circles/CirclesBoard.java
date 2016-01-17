package greycirclegames.games.board.circles;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

public class CirclesBoard extends ReflectionDBObject {

    private String[][] board;

    public CirclesBoard(int rows, int columns) {
        board = new String[rows][columns];
    }

    public CirclesBoard(BasicDBObject obj, int rows, int columns) {
        if(board == null) {
            board = new String[rows][columns];
        }
        BasicDBList board = (BasicDBList)obj.get("Board");
		int i = 0;
		for(Object row : board){
			int j = 0;
			for(Object cell : (BasicDBList) row){
				if(cell == null){
					setCell(i, j, null);
				} else {
					setCell(i, j, (String)cell);
				}
				j++;
			}
			i++;
		}
    }

    public int rows() {
        return getBoard().length;
    }

    public int columns() {
        return getBoard()[0].length;
    }

    public String cellAt(int row, int column) {
        return board[row][column];
    }

    public void setCell(int row, int column, String color) {
        board[row][column] = color;
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public CirclesBoard copy() {
        CirclesBoard copy = new CirclesBoard(rows(), columns());
        for(int i = 0; i < copy.rows(); i++) {
            for(int j = 0; j < copy.columns(); j++) {
                copy.setCell(i, j, cellAt(i, j));
            }
        }
        return copy;
    }
}
