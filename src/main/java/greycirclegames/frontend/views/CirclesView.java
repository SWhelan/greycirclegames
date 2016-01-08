package greycirclegames.frontend.views;

import java.util.ArrayList;
import java.util.List;

import greycirclegames.User;
import greycirclegames.games.board.circles.CirclePiece;
import greycirclegames.games.board.circles.Circles;
import greycirclegames.games.board.circles.CirclesGameState;

public class CirclesView {
	public List<RowView> displayBoard = new ArrayList<RowView>();
	public int gameId; 
	public String name;
	public String hex;
	public boolean isTurn;
	public boolean isActive;
	public int yourCount;
	public int theirCount;
	public String yourColor;
	public String theirColor;
	public String opponentName;
	public boolean isWinner = false;

	public CirclesView(Circles game, User user) {
		CirclePiece[][] board = ((CirclesGameState)game.getGameState()).getBoard();
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
		name = game.turnColors.get(game.currentPlayerIndex).getName();
		hex = game.turnColors.get(game.currentPlayerIndex).getHex();
		int currentPlayerIdCheck = game.getCurrentPlayerObject().get_id();
		int viewingPlayerIdCheck = user.get_id();
		isTurn = currentPlayerIdCheck == viewingPlayerIdCheck; 
		isActive = game.getIsActive();
		
		int light = 0;
		int dark = 0;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j] != null && board[i][j].getHex().equals("#ffffff")){
					light++;
				} else if(board[i][j] != null) {
					dark++;
				}
			}
		}
		if((int) game.getPlayers().get(0).get_id() == (int)user.get_id()){
			yourCount = light;
			theirCount = dark;
			yourColor = "#ffffff";
			theirColor = "#000000";
			opponentName = game.getPlayers().get(1).getUserName();
		} else {
			yourCount = dark; 
			theirCount = light;
			yourColor = "#000000";
			theirColor = "#ffffff";
			opponentName = game.getPlayers().get(0).getUserName();
		}
		if(!isActive){
			int winnerId = game.getWinner_id();
			if(winnerId == (int) user.get_id()){
				isWinner = true;
			}
		}
		
	}

}
