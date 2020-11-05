package games.board.circles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import greycirclegames.GlobalConstants;
import greycirclegames.games.board.circles.Circles;
import greycirclegames.games.board.circles.CirclesBoard;
import greycirclegames.games.board.circles.CirclesGameState;
import spark.utils.Assert;

public class CirclesTest {
	private static final List<Integer> players = new ArrayList<Integer>(Arrays.asList(-1, -2));

	public void testCircles(){
		Circles game = new Circles(2, players);
		game.applyAIMoves();
		Assert.isTrue(!game.getIsActive(), "Two player game that always ends.");
		Assert.isTrue(game.getGameTypeIdentifier().equals(GlobalConstants.CIRCLES), "Trivial test.");
	}
	
	public void testCirclesTie(){
		Circles game = new Circles(3, players);
		CirclesGameState gameState = game.getGameState();
		CirclesBoard board = gameState.getBoard();
		String w = GlobalConstants.COLOR.WHITE;
		String b = GlobalConstants.COLOR.BLACK;
		String[][] spoofBoard = 
			{
					{w, w, w, w, w, w, w, w},
					{w, w, w, w, w, w, w, w},
					{w, w, w, w, w, w, w, w},
					{w, w, w, w, w, w, w, w},
					{b, b, b, b, b, b, b, b},
					{b, b, b, b, b, b, b, b},
					{b, b, b, b, b, b, b, b},
					{b, b, b, b, b, b, b, b}
			};
		board.setBoard(spoofBoard);
		game.endTurn();
		Assert.isTrue(game.getTie(), "Should be a tie.");
	}

    public void testCirclesSkipTurn(String[] args) {
        Circles game = new Circles(4, players);
        CirclesGameState gameState = game.getGameState();
        CirclesBoard board = gameState.getBoard();
        String w = GlobalConstants.COLOR.WHITE;
        String b = GlobalConstants.COLOR.BLACK;
        String[][] spoofBoard =
                {
                        {null, b, w, w, w, w, w, w},
                        {b, b, b, b, b, b, b, b},
                        {b, b, b, b, b, b, b, b},
                        {b, b, b, b, b, b, b, b},
                        {b, b, b, b, b, b, b, b},
                        {b, b, b, b, b, b, b, b},
                        {b, b, b, b, b, b, b, b},
                        {b, b, b, b, b, b, b, b}
                };
        board.setBoard(spoofBoard);
        int currentPlayer = game.currentPlayerIndex;
        game.endTurn();
        Assert.isTrue(game.currentPlayerIndex == currentPlayer, "We skipped the second person's turn, so it should be the first person's turn again.");
    }
}
