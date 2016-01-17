package greycirclegames.games.board.circles;

import java.util.List;

import com.mongodb.BasicDBObject;

import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.games.GameState;

public class CirclesGameState extends GameState {
	private static final int ROWS = GlobalConstants.CIRCLES_ROWS;
	private static final int COLUMNS = GlobalConstants.CIRCLES_COLUMNS;
	private CirclesBoard board;

	public CirclesGameState(){
		super();
	}
	
	public CirclesGameState(BasicDBObject obj) {
		board = new CirclesBoard((BasicDBObject)obj.get("Board"), ROWS, COLUMNS);
	}

	@Override
	public void initializeToNewGameState(List<Player> players) {
		board = new CirclesBoard(ROWS, COLUMNS);
		this.setTurnNumber(players.get(0).get_id());
		for(int i = 0; i < board.rows()-1; i++){
			for(int j = 0; j < board.columns()-1; j++){
				// TODO what if we change board size?
				if((i == 3 && j == 3) || (i == 4 && j == 4)){
					board.setCell(i, j, GlobalConstants.COLOR.WHITE);
				} else if((i == 4 && j == 3) || (i == 3 && j == 4)){
					board.setCell(i, j, GlobalConstants.COLOR.BLACK);
				} else {
					board.setCell(i, j, null);
				}
			}
		}
	}
	
	public void setBoard(CirclesBoard board){
		this.board = board;
	}

	public CirclesBoard getBoard() {
		return board;
	}
	
	public boolean setPiece(int column, int row, String color){
		if(!validPosition(column, row)){
			return false;
		}
		board.setCell(row, column, color);
		return true;
	}
	
	public static boolean validPosition(int column, int row){
		return column <= COLUMNS && column >= 0 && row <= ROWS && row >= 0;
	}

}
