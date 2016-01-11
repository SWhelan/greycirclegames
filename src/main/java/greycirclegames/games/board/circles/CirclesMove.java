package greycirclegames.games.board.circles;

import com.mongodb.BasicDBObject;

import greycirclegames.games.Move;

public class CirclesMove extends Move {
	private int column;
	private int row;
	private String color;
	private CirclesGameState state;
	
	public CirclesMove(int row, int column, String color, CirclesGameState state){
		this.column = column;
		this.row = row;
		this.color = color;
		this.state = state;
	}

	public CirclesMove(BasicDBObject move) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValid() {
		if(!CirclesGameState.validPosition(column, row)){
			return false;
		}
		String[][] board = state.getBoard();
		return 	shouldApplyNorth(board) || shouldApplyNorthEast(board) ||
				shouldApplySouth(board) || shouldApplySouthEast(board) ||
				shouldApplyWest(board) || shouldApplySouthWest(board) ||
				shouldApplyEast(board) || shouldApplyNorthWest(board);
	}

	@Override
	public void apply() {
		// I wrote this while watching star wars so probably not right.
		String[][] board = state.getBoard();
		// North
		if(shouldApplyNorth(board)){
			applyNorth();
		}
		// North East
		if(shouldApplyNorthEast(board)){
			applyNorthEast();
		}
		// East
		if(shouldApplyEast(board)){
			applyEast();
		}		
		// South East
		if(shouldApplySouthEast(board)){
			applySouthEast();
		}
		// South
		if(shouldApplySouth(board)){
			applySouth();
		}
		// South West
		if(shouldApplySouthWest(board)){
			applySouthWest();
		}
		// West
		if(shouldApplyWest(board)){
			applyWest();
		}
		// North West
		if(shouldApplyNorthWest(board)){
			applyNorthWest();
		}
		state.getBoard()[row][column] = color;
	}

	private void applyNorthWest() {
		applyDirection(true, false, true, false);		
	}

	private boolean shouldApplyNorthWest(String[][] board) {
		return shouldApplyDirection(board, true, false, true, false);
	}

	private void applyWest() {
		applyDirection(false, false, true, false);		
	}

	private boolean shouldApplyWest(String[][] board) {
		return shouldApplyDirection(board, false, false, true, false);
	}

	private void applySouthWest() {
		applyDirection(false, true, true, false);
	}

	private boolean shouldApplySouthWest(String[][] board) {
		return shouldApplyDirection(board, false, true, true, false);
	}

	private void applySouth() {
		applyDirection(false, true, false, false);	
	}

	private boolean shouldApplySouth(String[][] board) {
		return shouldApplyDirection(board, false, true, false, false);
	}

	private void applySouthEast() {
		applyDirection(false, true, false, true);	
	}

	private boolean shouldApplySouthEast(String[][] board) {
		return shouldApplyDirection(board, false, true, false, true);
	}

	private void applyEast() {
		applyDirection(false, false, false, true);
	}

	private boolean shouldApplyEast(String[][] board) {
		return shouldApplyDirection(board, false, false, false, true);
	}

	private void applyNorthEast() {
		applyDirection(true, false, false, true);
	}

	private boolean shouldApplyNorthEast(String[][] board) {
		return shouldApplyDirection(board, true, false, false, true);
	}

	private void applyNorth() {
		applyDirection(true, false, false, false);
	}

	private boolean shouldApplyNorth(String[][] board) {
		return shouldApplyDirection(board, true, false, false, false);
	}
	
	private void applyDirection(boolean rowDecreases, boolean rowIncreases, boolean columnDecreases, boolean columnIncreases) {
		boolean done = false;
		int i = row;
		int j = column;
		while(!done && i > -1 && i < state.getBoard().length && j > -1 && j < state.getBoard()[i].length){
			String cellColor = state.getBoard()[i][j];
			if(cellColor != null && cellColor.equals(color)){
				done = true;
			} else if(i != row || j != column){
				state.getBoard()[i][j] = color;
			}
			if(rowDecreases){
				i = i - 1;
			}
			if(rowIncreases){
				i = i + 1;
			}
			if(columnDecreases){
				j = j - 1;
			}
			if(columnIncreases){
				j = j + 1;
			}
		}
	}
	
	private boolean shouldApplyDirection(String[][]board, boolean south, boolean north, boolean west, boolean east){
 		boolean result = false;
		boolean done = false;
		int i = row;
		int j = column;
		int count = 0;
		while(!done && i > -1 && i < board.length && j > -1 && j < board[i].length){
			String cellColor = board[i][j];
			if((i != row || j != column) && cellColor == null){
				done = true;
				result = false;
			} else if(cellColor != null && cellColor.equals(color)){
				done = true;
				result = true;
			}
			if(cellColor != null && !cellColor.equals(color)){
				count++;
			}
			if(south){
				i = i - 1;
			}
			if(north){
				i = i + 1;
			}
			if(west){
				j = j - 1;
			}
			if(east){
				j = j + 1;
			}
		}
		return result && count > 0;
	}
	

}
