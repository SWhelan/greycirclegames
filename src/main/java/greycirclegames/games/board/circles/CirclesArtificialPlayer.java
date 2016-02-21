package greycirclegames.games.board.circles;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

import greycirclegames.ArtificialPlayer;
import greycirclegames.GlobalConstants;
import greycirclegames.games.Game;
import greycirclegames.games.GameState;
import greycirclegames.games.Move;

public class CirclesArtificialPlayer extends ArtificialPlayer {

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
                possibleMove = new CirclesMove(i, j, GlobalConstants.COLOR.BLACK, state, this.get_id());
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
        CirclesMove copiedMove = new CirclesMove(move.getRow(), move.getColumn(), move.getColor(), copiedState, this.get_id());
        return circlesCaptured(copiedMove, copiedState);
    }

    private int circlesCaptured(CirclesMove move, CirclesGameState state) {
        CirclesGameState originalState = state.copy();
        String color = move.getColor();

        move.apply();
        int originalNumOnBoard = originalState.getNumOnBoard(color);
        int newNumOnBoard = state.getNumOnBoard(color);
        return newNumOnBoard - originalNumOnBoard;
    }

}
