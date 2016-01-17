package greycirclegames.games.board.circles;

import com.mongodb.BasicDBObject;

import greycirclegames.Player;
import greycirclegames.User;
import greycirclegames.games.Move;

public class CirclesMove extends Move {
	private int column;
	private int row;
	private String color;
	private CirclesGameState state;
	
	public CirclesMove(int row, int column, String color, CirclesGameState state, Player player){
		this.column = column;
		this.row = row;
		this.color = color;
		this.state = state;
		this.player = player;
	}

	public CirclesMove(BasicDBObject obj) {
		this.column = (Integer)obj.get("Column");
		this.row = (Integer)obj.get("Row");
		this.color = (String)obj.get("Color");
		BasicDBObject player = (BasicDBObject)obj.get("Player");
		Integer playerId = (Integer)player.get("_id");
		if(playerId < 0){
			this.player = new CirclesArtificialPlayer(playerId);
		} else {
			this.player = new User(player);
		}
	}

	@Override
	public boolean isValid() {
		if(!CirclesGameState.validPosition(column, row)){
			return false;
		}
		CirclesBoard board = state.getBoard();
		return 	shouldApplyNorth(board) || shouldApplyNorthEast(board) ||
				shouldApplySouth(board) || shouldApplySouthEast(board) ||
				shouldApplyWest(board) || shouldApplySouthWest(board) ||
				shouldApplyEast(board) || shouldApplyNorthWest(board);
	}

	@Override
	public void apply() {
		// I wrote this while watching star wars so probably not right.
		CirclesBoard board = state.getBoard();
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
		
		// Set the one they clicked last because the validation depends on that being null
		board.setCell(row, column, color);
	}

	private void applyNorthWest() {
		applyDirection(true, false, true, false);		
	}

	private boolean shouldApplyNorthWest(CirclesBoard board) {
		return shouldApplyDirection(board, true, false, true, false);
	}

	private void applyWest() {
		applyDirection(false, false, true, false);		
	}

	private boolean shouldApplyWest(CirclesBoard board) {
		return shouldApplyDirection(board, false, false, true, false);
	}

	private void applySouthWest() {
		applyDirection(false, true, true, false);
	}

	private boolean shouldApplySouthWest(CirclesBoard board) {
		return shouldApplyDirection(board, false, true, true, false);
	}

	private void applySouth() {
		applyDirection(false, true, false, false);	
	}

	private boolean shouldApplySouth(CirclesBoard board) {
		return shouldApplyDirection(board, false, true, false, false);
	}

	private void applySouthEast() {
		applyDirection(false, true, false, true);	
	}

	private boolean shouldApplySouthEast(CirclesBoard board) {
		return shouldApplyDirection(board, false, true, false, true);
	}

	private void applyEast() {
		applyDirection(false, false, false, true);
	}

	private boolean shouldApplyEast(CirclesBoard board) {
		return shouldApplyDirection(board, false, false, false, true);
	}

	private void applyNorthEast() {
		applyDirection(true, false, false, true);
	}

	private boolean shouldApplyNorthEast(CirclesBoard board) {
		return shouldApplyDirection(board, true, false, false, true);
	}

	private void applyNorth() {
		applyDirection(true, false, false, false);
	}

	private boolean shouldApplyNorth(CirclesBoard board) {
		return shouldApplyDirection(board, true, false, false, false);
	}
	
	private void applyDirection(boolean south, boolean north, boolean west, boolean east) {
		CirclesBoard board = state.getBoard();
		boolean done = false;
		int i = row;
		int j = column;
		while(!done && i > -1 && i < board.rows() && j > -1 && j < board.columns()){
			String cellColor = board.cellAt(i, j);
			if(cellColor != null && cellColor.equals(color)){
				done = true;
			} else if(i != row || j != column){
				board.setCell(i, j, color);
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
	}
	
	private boolean shouldApplyDirection(CirclesBoard board, boolean south, boolean north, boolean west, boolean east){
 		boolean result = false;
		boolean done = false;
		int i = row;
		int j = column;
		int count = 0;
		while(!done && i > -1 && i < board.rows() && j > -1 && j < board.columns()){
			String cellColor = board.cellAt(i, j);
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

	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
