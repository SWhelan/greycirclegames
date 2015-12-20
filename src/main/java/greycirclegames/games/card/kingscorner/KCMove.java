package greycirclegames.games.card.kingscorner;

import com.mongodb.BasicDBObject;

import greycirclegames.GlobalConstants;
import greycirclegames.Player;
import greycirclegames.User;
import greycirclegames.games.card.Card;
import greycirclegames.games.card.CardBasedMove;
import greycirclegames.games.card.Pile;

/**
 * A move for King's Corner.
 * @author George
 *
 */
public class KCMove extends CardBasedMove {

	public KCMove(Player player, Pile origin, Pile moving, Pile destination){
		super(player, origin, moving, destination);
	}

	public KCMove(BasicDBObject obj) {
		this.origin = new Pile((BasicDBObject)obj.get("Origin"));
		this.moving = new Pile((BasicDBObject)obj.get("Moving"));
		this.destination = new Pile((BasicDBObject)obj.get("Destination"));
		BasicDBObject player = (BasicDBObject)obj.get("Player");
		Integer playerId = (Integer)player.get("_id");
		if(playerId < 0){
			this.player = new ArtificialPlayer(playerId);
		} else {
			this.player = new User(player);
		}
	}

	@Override
	public boolean isValid() {
		//If this is a draw move
		if(origin.getName().equals("Draw Pile")){
			//The moving pile should be 1 card, and it should equal the top card of the draw pile
			return moving.size() == 1 && origin.getTop().equals(moving.getTop());
		}
		//If everything in moving is in origin, and if the bottom card of moving works with the top of destination, we good
		if(origin.containsAll(moving)){
			Card movingBottom = moving.getBottom();
			Card destTop = null;
			if(destination.size() > 0){
				destTop = destination.getTop();
			}
			boolean destIsCorner = destIsCorner(destination);
			if(cardsAreCompatible(movingBottom, destTop, destIsCorner)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean destIsCorner(Pile dest){
		return 	dest.getName().equals("Northeast Pile") ||
				dest.getName().equals("Northwest Pile") ||
				dest.getName().equals("Southeast Pile") ||
				dest.getName().equals("Southwest Pile");
	}

	private static boolean cardsAreCompatible(Card movingBottom, Card destTop, Boolean destIsCorner) {
		//If the destination is empty
		if(destTop == null){
			//We can move any card there if it is not a corner.
			//If it is a corner, the moving pile must have a King on the bottom
			return (!destIsCorner || (destIsCorner && movingBottom.getNumber() == GlobalConstants.KING));
		}
		boolean numbersCompatible = movingBottom.getNumber() == destTop.getNumber() - 1;
		boolean suitsCompatible = movingBottom.isRed() ^ destTop.isRed();
		return numbersCompatible && suitsCompatible;
	}

	@Override
	public void apply() {
		//Remove moving from origin, add to destination
		if(!isValid()){
			throw new IllegalStateException("Cannot apply an invalid move.");
		}
		origin.removeAll(moving);
		destination.addAll(moving);
	}

	
}
