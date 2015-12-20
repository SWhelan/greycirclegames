package greycirclegames.games.circles;

import java.util.List;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;

import greycirclegames.Player;
import greycirclegames.games.GameState;

public class CirclesGameState extends GameState {
	private static final int ROWS = 8;
	private static final int COLUMNS = 8;
	private Circle[][] board = new Circle[ROWS][COLUMNS];
	
	public CirclesGameState(){
		super();
	}
	
	public CirclesGameState(BasicDBObject obj) {
		BasicDBObject board = (BasicDBObject)obj.get("Board");
		for(Entry<String, Object> entry : board.entrySet()){
			System.out.println(entry);
		}
		System.out.println();
	}

	@Override
	public void initializeToNewGameState(List<Player> players) {
		this.setTurnNumber(players.get(0).get_id());
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				// TODO what if we change board size?
				if((i == 3 && j == 3) || (i == 4 && j == 4)){
					board[i][j] = new Circle("White", "#ffffff");
				} else if((i == 4 && j == 3) || (i == 3 && j == 4)){
					board[i][j] = new Circle("Black", "#000000");
				} else {
					board[i][j] = null;
				}
			}
		}
	}
	
	public void setBoard(Circle[][] board){
		this.board = board;
	}

	public Circle[][] getBoard() {
		return board;
	}
	
	public boolean setPiece(int column, int row, Circle piece){
		if(!validPosition(column, row)){
			return false;
		}
		board[row][column] = piece;
		return true;
	}
	
	public static boolean validPosition(int column, int row){
		return column <= COLUMNS && column >= 0 && row <= ROWS && row >= 0;
	}

}
