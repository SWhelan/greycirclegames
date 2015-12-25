package greycirclegames.frontend.views;

import java.util.ArrayList;
import java.util.List;

import greycirclegames.User;
import greycirclegames.games.board.circles.Circle;
import greycirclegames.games.board.circles.Circles;
import greycirclegames.games.board.circles.CirclesGameState;

public class CirclesView {
	public List<RowView> displayBoard = new ArrayList<RowView>();
	public int gameId; 
	public String name;
	public String hex;

	public CirclesView(Circles game, User user) {
		Circle[][] board = ((CirclesGameState)game.getGameState()).getBoard();
		for(int i = 0; i < board.length; i++){		
			ArrayList<CircleView> row = new ArrayList<CircleView>();
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j] == null){
					row.add(new CircleView(i, j, "", false));
				} else {
					row.add(new CircleView(i, j, board[i][j].getHex(), true));
				}
			}
			displayBoard.add(new RowView(row));
		}
		gameId = game.get_id();
		name = game.turnColors.get(game.currentPlayer).getName();
		hex = game.turnColors.get(game.currentPlayer).getHex();
	}

}
