package greycirclegames.frontend.views;

import java.util.ArrayList;
import java.util.List;

import greycirclegames.GlobalConstants;
import greycirclegames.User;
import greycirclegames.games.board.circles.Circles;
import greycirclegames.games.board.circles.CirclesGameState;
import greycirclegames.games.board.circles.CirclesMove;

public class CirclesView {
	public List<RowView> displayBoard = new ArrayList<RowView>();
	public int gameId; 
	public String name;
	public String color;
	public boolean isTurn;
	public boolean isActive;
	public int yourCount;
	public int theirCount;
	public String yourColor;
	public String theirColor;
	public String opponentName;
	public boolean isWinner = false;

	public CirclesView(Circles game, User user) {
	    CirclesGameState gameState = (CirclesGameState)game.getGameState();
		String[][] board = gameState.getBoard();
		gameId = game.get_id();
		color = game.turnColors.get(game.currentPlayerIndex);
		int currentPlayerId = game.getCurrentPlayerObject().get_id();
		int viewingPlayerId = user.get_id();
		isTurn = currentPlayerId == viewingPlayerId; 
		isActive = game.getIsActive();
		
		int lightCount = 0;
		int darkCount = 0;
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				if(board[i][j] != null && board[i][j].equals(GlobalConstants.COLOR.WHITE)){
					lightCount++;
				} else if(board[i][j] != null) {
					darkCount++;
				}
			}
		}
		if((int) game.getPlayers().get(0).get_id() == viewingPlayerId){
			yourCount = lightCount;
			theirCount = darkCount;
			yourColor = GlobalConstants.COLOR.WHITE;
			theirColor = GlobalConstants.COLOR.BLACK;
			opponentName = game.getPlayers().get(1).getUsername();
		} else {
			yourCount = darkCount; 
			theirCount = lightCount;
			yourColor = GlobalConstants.COLOR.BLACK;
			theirColor = GlobalConstants.COLOR.WHITE;
			opponentName = game.getPlayers().get(0).getUsername();
		}
		if(!isActive){
			int winnerId = game.getWinner_id();
			if(winnerId == (int) viewingPlayerId){
				isWinner = true;
			}
		}
		
		for(int i = 0; i < board.length; i++){
            ArrayList<CircleView> row = new ArrayList<CircleView>();
            for(int j = 0; j < board[i].length; j++){
                if(board[i][j] == null){
                    if(new CirclesMove(i, j, yourColor, gameState).isValid()) {
                        row.add(new CircleView(i, j, true));
                    } else {
                        row.add(new CircleView(i, j, false));
                    }
                } else {
                    row.add(new CircleView(i, j, board[i][j], true));
                }
            }
            displayBoard.add(new RowView(row));
        }
	}

}
