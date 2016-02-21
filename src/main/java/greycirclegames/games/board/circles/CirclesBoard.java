package greycirclegames.games.board.circles;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.ReflectionDBObject;

import java.util.HashMap;
import java.util.Map;

public class CirclesBoard extends ReflectionDBObject {

    private String[][] board;
    private Map<String, Integer> numOnBoard = new HashMap<String, Integer>();

    public CirclesBoard(int rows, int columns) {
        board = new String[rows][columns];
        for(int i = 0; i < Circles.turnColors.size(); i++) {
            numOnBoard.put(Circles.turnColors.get(i), 0);
        }
    }

    public CirclesBoard(BasicDBObject obj, int rows, int columns) {
        this(rows, columns);
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
        String originalColor = board[row][column];
        board[row][column] = color;

        if(color != null && !color.equals(originalColor)) {
            numOnBoard.put(color, numOnBoard.get(color)+1);
            if(originalColor != null) {
                numOnBoard.put(originalColor, numOnBoard.get(originalColor) - 1);
            }
        }
    }

    public String[][] getBoard() {
        return board;
    }

    public int getNumOnBoard(String color) {
        return numOnBoard.get(color);
    }

    public void setBoard(String[][] board) {
        for(int i = 0; i < rows(); i++) {
            for(int j = 0; j < columns(); j++) {
                setCell(i, j, board[i][j]);
            }
        }
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < rows(); i++) {
            for(int j = 0; j < columns(); j++) {
                builder.append(cellAt(i, j) + ", ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
