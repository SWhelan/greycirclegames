package greycirclegames.games.board.circles;

import greycirclegames.games.GameState;
import greycirclegames.games.card.CardBasedMove;

public class CirclesMove extends CardBasedMove {
	private int column;
	private int row;
	private Circle color;
	private GameState state;
	
	public CirclesMove(int column, int row, Circle color, CirclesGameState state){
		this.column = column;
		this.row = row;
		this.color = color;
		this.state = state;
	}

	@Override
	public boolean isValid() {
		if(!CirclesGameState.validPosition(column, row)){
			return false;
		}
		// TODO lots of logic
		return true;
	}

	@Override
	public void apply() {
		// TODO
	}

}
