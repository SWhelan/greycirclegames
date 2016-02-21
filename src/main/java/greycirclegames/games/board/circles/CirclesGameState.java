package greycirclegames.games.board.circles;

import java.util.List;

import com.mongodb.BasicDBObject;

import greycirclegames.ArtificialPlayer;
import greycirclegames.GlobalConstants;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;
import greycirclegames.games.Move;

public class CirclesGameState extends GameState {
	private static final int ROWS = GlobalConstants.CIRCLES_ROWS;
	private static final int COLUMNS = GlobalConstants.CIRCLES_COLUMNS;
	private CirclesBoard board;

	public CirclesGameState(){
		super();
	}

    public CirclesGameState(CirclesBoard board) {
        this.board = board;
    }

	public CirclesGameState(BasicDBObject obj) {
		board = new CirclesBoard((BasicDBObject)obj.get("Board"), ROWS, COLUMNS);
	}

	@Override
	public void initializeToNewGameState(Game<? extends Move, ? extends GameState, ? extends ArtificialPlayer> game, List<Integer> players) {
		board = new CirclesBoard(ROWS, COLUMNS);
		this.setTurnNumber(players.get(0));
		int centerHorizontally = Math.round(COLUMNS/2);
		int centerVertically = Math.round(ROWS/2);

		for(int i = 0; i < board.rows(); i++){
			for(int j = 0; j < board.columns(); j++){
				if((i == centerVertically-1 && j == centerHorizontally-1)
						|| (i == centerVertically && j == centerHorizontally)){
					board.setCell(i, j, GlobalConstants.COLOR.WHITE);
				} else if((i == centerVertically && j == centerHorizontally-1)
						|| (i == centerVertically-1 && j == centerHorizontally)){
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
	
	public boolean validPosition(int column, int row) {
        if(board.cellAt(row, column) != null) return false;
		return column <= COLUMNS && column >= 0 && row <= ROWS && row >= 0;
	}

    public int getNumOnBoard(String color) {
        return board.getNumOnBoard(color);
    }

    public CirclesGameState copy() {
        return new CirclesGameState(board.copy());
    }
}
