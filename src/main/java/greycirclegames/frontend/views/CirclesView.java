package greycirclegames.frontend.views;

import java.util.ArrayList;
import java.util.List;

import greycirclegames.DBHandler;
import greycirclegames.GlobalConstants;
import greycirclegames.games.board.circles.Circles;
import greycirclegames.games.board.circles.CirclesArtificialPlayer;
import greycirclegames.games.board.circles.CirclesBoard;
import greycirclegames.games.board.circles.CirclesGameState;
import greycirclegames.games.board.circles.CirclesMove;

public class CirclesView {
	public List<RowView> displayBoard = new ArrayList<>();
	public int gameId;
	public String gameRoute;
	public String color;
	public boolean isTurn;
	public boolean isActive;
	public int yourCount;
	public int theirCount;
	public String yourColor;
	public String theirColor;
	public String opponentName;
	public boolean isWinner = false;
	public boolean isTie = false;

	public CirclesView(Circles game, Integer userId) {
	    CirclesGameState gameState = game.getGameState();
		CirclesBoard board = gameState.getBoard();
		gameId = game.get_id();
		color = Circles.turnColors.get(game.currentPlayerIndex);
		int currentPlayerId = game.getPlayers().get(game.currentPlayerIndex);
		int viewingPlayerId = userId;
		isTurn = currentPlayerId == viewingPlayerId; 
		isActive = game.getIsActive();
		
		int lightCount = gameState.getNumOnBoard(GlobalConstants.COLOR.WHITE);
		int darkCount = gameState.getNumOnBoard(GlobalConstants.COLOR.BLACK);

		if(game.getPlayers().get(0) == viewingPlayerId){
			yourCount = lightCount;
			theirCount = darkCount;
			yourColor = GlobalConstants.COLOR.WHITE;
			theirColor = GlobalConstants.COLOR.BLACK;
			int id = game.getPlayers().get(1); 
			if(id < 0){
				opponentName = new CirclesArtificialPlayer(id).getUsername();
			} else {
				opponentName = DBHandler.getUser(id).getUsername();
			}
		} else {
			yourCount = darkCount; 
			theirCount = lightCount;
			yourColor = GlobalConstants.COLOR.BLACK;
			theirColor = GlobalConstants.COLOR.WHITE;
			int id = game.getPlayers().get(0); 
			if(id < 0){
				opponentName = new CirclesArtificialPlayer(id).getUsername();
			} else {
				opponentName = DBHandler.getUser(id).getUsername();
			}
		}
		if(!isActive){
			int winnerId = game.getWinner_id();
			if(winnerId == viewingPlayerId){
				isWinner = true;
			}
			isTie = game.getTie();
		}
		
		boolean showHelpers = DBHandler.getUser(userId).getShowHelpers();
		
		for(int i = 0; i < board.rows(); i++){
            ArrayList<CircleView> row = new ArrayList<>();
            for(int j = 0; j < board.columns(); j++){
                if(board.cellAt(i, j) == null){
                    if(new CirclesMove(i, j, yourColor, gameState, userId).isValid()) {
                        row.add(new CircleView(i, j, true && showHelpers));
                    } else {
                        row.add(new CircleView(i, j, false));
                    }
                } else {
                    row.add(new CircleView(i, j, board.cellAt(i, j), true));
                }
            }
            displayBoard.add(new RowView(row));
        }
		this.gameRoute = game.getRootUrlRoute();
	}

}
