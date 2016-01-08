package greycirclegames.games.board.circles;

import com.mongodb.BasicDBObject;

import greycirclegames.games.card.CardBasedMove;

public class CirclesMove extends CardBasedMove {
	private int column;
	private int row;
	private CirclePiece color;
	private CirclesGameState state;
	
	public CirclesMove(int row, int column, CirclePiece color, CirclesGameState state){
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
		CirclePiece[][] board = state.getBoard();
		return 	shouldApplyNorth(board) || shouldApplyNorthEast(board) ||
				shouldApplySouth(board) || shouldApplySouthEast(board) ||
				shouldApplyWest(board) || shouldApplySouthWest(board) ||
				shouldApplyEast(board) || shouldApplyNorthWest(board);
	}

	@Override
	public void apply() {
		// I wrote this while watching star wars so probably not right.
		CirclePiece[][] board = state.getBoard();
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
		state.getBoard()[row][column] = new CirclePiece(color.getName(), color.getHex());
	}

	private void applyNorthWest() {
		applyDirection(true, false, true, false);		
	}

	private boolean shouldApplyNorthWest(CirclePiece[][] board) {
		return shouldApplyDirection(board, true, false, true, false);
	}

	private void applyWest() {
		applyDirection(false, false, true, false);		
	}

	private boolean shouldApplyWest(CirclePiece[][] board) {
		return shouldApplyDirection(board, false, false, true, false);
	}

	private void applySouthWest() {
		applyDirection(false, true, true, false);
	}

	private boolean shouldApplySouthWest(CirclePiece[][] board) {
		return shouldApplyDirection(board, false, true, true, false);
	}

	private void applySouth() {
		applyDirection(false, true, false, false);	
	}

	private boolean shouldApplySouth(CirclePiece[][] board) {
		return shouldApplyDirection(board, false, true, false, false);
	}

	private void applySouthEast() {
		applyDirection(false, true, false, true);	
	}

	private boolean shouldApplySouthEast(CirclePiece[][] board) {
		return shouldApplyDirection(board, false, true, false, true);
	}

	private void applyEast() {
		applyDirection(false, false, false, true);
	}

	private boolean shouldApplyEast(CirclePiece[][] board) {
		return shouldApplyDirection(board, false, false, false, true);
	}

	private void applyNorthEast() {
		applyDirection(true, false, false, true);
	}

	private boolean shouldApplyNorthEast(CirclePiece[][] board) {
		return shouldApplyDirection(board, true, false, false, true);
	}

	private void applyNorth() {
		applyDirection(true, false, false, false);
	}

	private boolean shouldApplyNorth(CirclePiece[][] board) {
		return shouldApplyDirection(board, true, false, false, false);
	}
	
	private void applyDirection(boolean rowDecreases, boolean rowIncreases, boolean columnDecreases, boolean columnIncreases) {
		boolean done = false;
		int i = row;
		int j = column;
		while(!done && i > -1 && i < state.getBoard().length && j > -1 && j < state.getBoard()[i].length){
			CirclePiece cell = state.getBoard()[i][j];
			if(cell != null && cell.getHex().equals(color.getHex())){
				done = true;
			} else if(i != row || j != column){
				state.getBoard()[i][j] = new CirclePiece(color.getName(), color.getHex());
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
	
	private boolean shouldApplyDirection(CirclePiece[][]board, boolean south, boolean north, boolean west, boolean east){
 		boolean result = false;
		boolean done = false;
		int i = row;
		int j = column;
		int count = 0;
		while(!done && i > -1 && i < board.length && j > -1 && j < board[i].length){
			CirclePiece cell = board[i][j];
			if((i != row || j != column) && cell == null){
				done = true;
				result = false;
			} else if(cell != null && cell.getHex().equals(color.getHex())){
				done = true;
				result = true;
			}
			if(cell != null && !cell.getHex().equals(color.getHex())){
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
