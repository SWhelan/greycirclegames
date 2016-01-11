package greycirclegames.games.board.circles;

import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.games.GameState;

public class CirclesGameState extends GameState {
	private static final int ROWS = GlobalConstants.CIRCLES_ROWS;
	private static final int COLUMNS = GlobalConstants.CIRCLES_COLUMNS;
	private String[][] board = new String[ROWS][COLUMNS];
	
	public CirclesGameState(){
		super();
	}
	
	public CirclesGameState(BasicDBObject obj) {
		BasicDBList board = (BasicDBList)obj.get("Board");
		int i = 0;
		for(Object row : board){
			int j = 0;
			for(Object cell : (BasicDBList) row){
				if(cell == null){
					this.board[i][j] = null;
				} else {
					this.board[i][j] = (String)cell;
				}
				j++;
			}
			i++;
		}
	}

	@Override
	public void initializeToNewGameState(List<Player> players) {
		this.setTurnNumber(players.get(0).get_id());
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				// TODO what if we change board size?
				if((i == 3 && j == 3) || (i == 4 && j == 4)){
					board[i][j] = GlobalConstants.COLOR.WHITE;
				} else if((i == 4 && j == 3) || (i == 3 && j == 4)){
					board[i][j] = GlobalConstants.COLOR.BLACK;
				} else {
					board[i][j] = null;
				}
			}
		}
	}
	
	public void setBoard(String[][] board){
		this.board = board;
	}

	public String[][] getBoard() {
		return board;
	}
	
	public boolean setPiece(int column, int row, String color){
		if(!validPosition(column, row)){
			return false;
		}
		board[row][column] = color;
		return true;
	}
	
	public static boolean validPosition(int column, int row){
		return column <= COLUMNS && column >= 0 && row <= ROWS && row >= 0;
	}

}
