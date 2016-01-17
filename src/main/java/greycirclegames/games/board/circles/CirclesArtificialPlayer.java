package greycirclegames.games.board.circles;

import com.google.common.collect.Multimap;
import greycirclegames.ArtificialPlayer;
import greycirclegames.GlobalConstants;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;
import greycirclegames.games.Move;

import com.google.common.collect.TreeMultimap;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class CirclesArtificialPlayer extends ArtificialPlayer {
    /**
     *
     */
    private static final long serialVersionUID = 2839749238749238L;

    public CirclesArtificialPlayer(int id){
        super(id);
    }

    @Override
    public Move createMove(Game<? extends Move, ? extends  GameState, ? extends ArtificialPlayer> game) {
        CirclesGameState state = (CirclesGameState)game.getGameState();
        CirclesBoard board = state.getBoard();

        Multimap<Integer, CirclesMove> moveRewards =
                TreeMultimap.create(
                        // numbers in descending order
                        (o1, o2) -> o2.compareTo(o1),
                        // moves in random order
                        (move1, move2) -> ThreadLocalRandom.current().nextInt(-1, 2));

        CirclesMove possibleMove;
        for(int i = 0; i < board.rows(); i++) {
            for(int j = 0; j < board.columns(); j++) {
                // TODO: if colors ever change we're screwed
                possibleMove = new CirclesMove(i, j, GlobalConstants.COLOR.BLACK, state, this);
                int moveReward = getMoveReward(possibleMove, state);
                moveRewards.put(moveReward, possibleMove);
            }
        }
        Collection<CirclesMove> sortedMoves = moveRewards.values();
        return sortedMoves.stream().findFirst().get();
    }

    private int getMoveReward(CirclesMove move, CirclesGameState state) {
        if(!move.isValid()) {
            return -Integer.MAX_VALUE;
        }
        CirclesGameState copiedState = state.copy();
        CirclesMove copiedMove = new CirclesMove(move.getRow(), move.getColumn(), move.getColor(), copiedState, this);
        return circlesCaptured(copiedMove, copiedState);
    }

    private int circlesCaptured(CirclesMove move, CirclesGameState state) {
        CirclesGameState originalState = state.copy();
        String color = move.getColor();

        move.apply();
        int originalNumOnBoard = originalState.numOnBoard(color);
        int newNumOnBoard = state.numOnBoard(color);
        return originalNumOnBoard - newNumOnBoard;
    }

}
